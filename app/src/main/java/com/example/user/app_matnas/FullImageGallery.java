package com.example.user.app_matnas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class FullImageGallery extends AppCompatActivity {


    private ImageView imageViewFull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_gallery);
        imageViewFull = (ImageView)findViewById(R.id.imageViewFull);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        String filename = extras.getString("filename");
       Bitmap mBitmap = BitmapFactory.decodeFile(filename, bfo);
        imageViewFull.setImageBitmap(mBitmap);
    }
}


