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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FullImageGallery extends AppCompatActivity {


    private ImageView imageViewFull;
    String image = "!!!!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_gallery);

        imageViewFull = (ImageView) findViewById(R.id.imageViewFull);
        image = getIntent().getStringExtra("full");

        TextView tv = (TextView)findViewById(R.id.textView3);
        tv.setText("image");
        imageViewFull.setScaleType(ImageView.ScaleType.CENTER);
        //Glide.with(this).load(image).into(imageViewFull);
        Glide.with(getApplicationContext()).load(image).into(imageViewFull);


    }

}



