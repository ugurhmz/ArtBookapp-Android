package com.ugurhmz.artbookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.ugurhmz.artbookapp.databinding.ActivityArtBinding;

public class ArtActivity extends AppCompatActivity {

    private ActivityArtBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


    public void saveClick(View view){

    }

    public void selectClickImage(View view) {

        // izini yoksa, kullanıcıdan Snackbar  ile izin iste
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();

                } else {
                        // request permission

                }

        }

        // Kullanıcının izni zaten vardır, androidin düşük sürümleri...
        else {  //gallery

        }


    }

}