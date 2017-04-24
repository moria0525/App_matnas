package com.example.user.app_matnas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FullImageGallery extends AppCompatActivity {


    private ImageView imageViewFull;
    String image,image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_gallery);

        imageViewFull = (ImageView) findViewById(R.id.imageViewFull);
         image = getIntent().getStringExtra("image");
        imageViewFull.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(this).load(image).into(imageViewFull);

//        Button b= (Button)findViewById(R.id.button2);
//        b.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"!",Toast.LENGTH_LONG).show();
//                image1 = getIntent().getStringExtra("image1");
//                imageViewFull.setScaleType(ImageView.ScaleType.CENTER);
//                Glide.with(getApplicationContext()).load(image1).into(imageViewFull);
//
//
//            }
//        });
    }

}



