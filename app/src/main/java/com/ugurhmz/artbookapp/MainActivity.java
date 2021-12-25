package com.ugurhmz.artbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ugurhmz.artbookapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); ;               //Binding için
        setContentView(binding.getRoot()); ;                                        //Binding için

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
            Intent intent = new Intent(this, ArtActivity.class);        // Mainactivity'den --> ArtActiviye git. tıklanınca
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}








