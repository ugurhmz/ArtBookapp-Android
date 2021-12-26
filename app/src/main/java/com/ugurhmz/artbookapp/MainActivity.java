package com.ugurhmz.artbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

    ArrayList<Art> artList;

    ArtAdapter artAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        artList = new ArrayList<>();

        binding.listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artAdapter = new ArtAdapter(artList);
        binding.listRecyclerView.setAdapter(artAdapter);


        getData();

    }


    // Get datas
    public void getData() {

        try {
            SQLiteDatabase database = this.openOrCreateDatabase("ArtsDB",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM arts", null);

            int idIx = cursor.getColumnIndex("id");
            int nameIx = cursor.getColumnIndex("art_name");


            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIx);
                String name = cursor.getString(nameIx);
                Art art = new Art(name,id);
                artList.add(art);
            }
            artAdapter.notifyDataSetChanged();

            cursor.close();
        } catch (Exception e) {
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
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);        // Mainactivity'den --> ArtActiviye git. tıklanınca
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}








