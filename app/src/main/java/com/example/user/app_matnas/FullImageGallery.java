package com.example.user.app_matnas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageGallery extends AppCompatActivity {


    private ImageView imageViewFull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_gallery);

        Intent i = getIntent();

        int position = i.getExtras().getInt("id");
        GalleryAdapter adapter = new GalleryAdapter(this);

        imageViewFull  = (ImageView) findViewById(R.id.imageViewFull);
        imageViewFull.setImageResource(adapter.images[position]);
    }
}
