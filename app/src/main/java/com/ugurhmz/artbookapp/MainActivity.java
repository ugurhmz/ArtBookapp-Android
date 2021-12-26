package com.ugurhmz.artbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ugurhmz.artbookapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public ArrayList<Art> artArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        artArrayList = new ArrayList<>();

    }


    // Get datas
    private void getData(){
            try {

                SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("ArtsDB",MODE_PRIVATE, null);

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM arts", null);
                int nameIx = cursor.getColumnIndex("art_name");
                int idIx = cursor.getColumnIndex("id");

                while(cursor.moveToNext()){
                    String name = cursor.getString(nameIx);
                    int id = cursor.getInt(idIx);
                    Art art = new Art(id,name);
                    artArrayList.add(art);
                }

                cursor.close();

            } catch (Exception  e) {
                e.printStackTrace();
            }
    }




    // art_menu.xml'i bağlamak
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.art_menu, menu);        //xml adı, menu


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_art){           //itemin id'si == ise art_menu.xml içindeki itemin id'sine.
            Intent intent = new Intent(MainActivity.this, ArtActivity.class);        // Mainactivity'den --> ArtActiviye git. tıklanınca
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}








