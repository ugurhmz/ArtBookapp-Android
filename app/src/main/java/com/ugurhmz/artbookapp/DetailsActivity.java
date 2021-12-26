package com.ugurhmz.artbookapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ugurhmz.artbookapp.databinding.ActivityArtBinding;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    Bitmap selectedImage;
    SQLiteDatabase database;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private ActivityArtBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();

        database = this.openOrCreateDatabase("ArtsDB",MODE_PRIVATE,null);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");



        if(info.matches("new")){
            binding.nameText.setText("");
            binding.artistText.setText("");
            binding.yearText.setText("");
            binding.saveBtn.setVisibility(View.VISIBLE);

            Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.select_image);
            binding.selectImage.setImageBitmap(selectImage);
        }else {
            int artId = intent.getIntExtra("artId",1);
            binding.saveBtn.setVisibility(View.INVISIBLE);

            try {

                Cursor cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?",new String[] {String.valueOf(artId)});

                int artNameIx = cursor.getColumnIndex("art_name");
                int painterNameIx = cursor.getColumnIndex("painter_name");
                int yearIx = cursor.getColumnIndex("year");
                int imageIx = cursor.getColumnIndex("image");

                while (cursor.moveToNext()) {

                    binding.nameText.setText(cursor.getString(artNameIx));
                    binding.artistText.setText(cursor.getString(painterNameIx));
                    binding.yearText.setText(cursor.getString(yearIx));

                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    binding.selectImage.setImageBitmap(bitmap);


                }

                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    // Save button
    public void saveClick(View view){

        String name = binding.nameText.getText().toString();
        String artistName = binding.artistText.getText().toString();
        String year = binding.yearText.getText().toString();


        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50, outputStream);
        byte[] byteArray = outputStream.toByteArray();      // SQLite kaydedilecek, fotoyu 1001.. şekline çevirdik.


        // DB'YE KAYDET
        try {
            database = this.openOrCreateDatabase("ArtsDB",MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS arts (" +
                    "id INTEGER PRIMARY KEY, " +
                    "art_name VARCHAR, " +
                    "painter_name VARCHAR, " +
                    "year VARCHAR," +
                    "image BLOB) ");


            String sqlString = "INSERT INTO arts (art_name, painter_name, year, image) VALUES(?,?,?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1, name);
            sqLiteStatement.bindString(2, artistName);
            sqLiteStatement.bindString(3, year);
            sqLiteStatement.bindBlob(4, byteArray);

            sqLiteStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }


    // Make smaller image
    public Bitmap makeSmallerImage(Bitmap image, int maxSize){
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;

            if(bitmapRatio > 1){
                    width = maxSize;
                    height = (int) (width / bitmapRatio );

            } else {
                height = maxSize;
                width = (int) ( height * bitmapRatio);
            }

            return image.createScaledBitmap(image, width, height, true);
    }



    // Click select image
    public void selectClickImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }



    // registerLauncheri oluştur -> onCreate altında çağır.
    public void registerLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intentFromResult = result.getData();
                            if (intentFromResult != null) {
                                Uri imageData = intentFromResult.getData();
                                try {

                                    if (Build.VERSION.SDK_INT >= 28) {
                                        ImageDecoder.Source source = ImageDecoder.createSource(DetailsActivity.this.getContentResolver(),imageData);
                                        selectedImage = ImageDecoder.decodeBitmap(source);
                                        binding.selectImage.setImageBitmap(selectedImage);

                                    } else {
                                        selectedImage = MediaStore.Images.Media.getBitmap(DetailsActivity.this.getContentResolver(),imageData);
                                        binding.selectImage.setImageBitmap(selectedImage);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                });


        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            //izin verildi.
                            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intentToGallery);

                        } else {
                            //izin verilmedi
                            Toast.makeText(DetailsActivity.this,"Permisson needed!",Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }



}
















