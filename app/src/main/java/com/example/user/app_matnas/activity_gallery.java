package com.example.user.app_matnas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class activity_gallery extends AppCompatActivity {
    private GridView gv;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gv = (GridView)findViewById(R.id.gallery_grid);
        gv.setAdapter(new GalleryAdapter(this));


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent i = new Intent(getApplicationContext(),FullImageGallery.class);
                i.putExtra("id",position);
                startActivity(i);

            }
        });

    }
}
