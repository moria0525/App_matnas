package com.example.user.app_matnas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity represents an one album in gallery
 * Allows to enter each image and swipe between images and more
 */

public class Album extends AppCompatActivity {

    private ArrayList<String> urlList;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);

        //get intent from activity_gallery.class
        Intent i = getIntent();
        String image = i.getStringExtra("category");

        //find if id's fields and init variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(image);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);
        urlList = new ArrayList<>();

        //get urls of images from DB
        mDatabaseRef.child(DB_IMAGE + "/").child(image).addValueEventListener
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         int i = 0;
                         Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                         int length = (int) dataSnapshot.getChildrenCount();
                         String[] url = new String[length];
                         while (i < length) {
                             url[i] = iterator.next().getValue().toString();
                             urlList.add(url[i]);
                             i++;
                         }

                         mAdapter = new GalleryAdapter(Album.this, urlList);
                         mRecyclerView.setAdapter(mAdapter);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 }
                );

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        //pass intent to show image selected
                        Intent intent = new Intent(Album.this, FullImage.class);
                        intent.putExtra("data", urlList);
                        intent.putExtra("pos", position);
                        startActivity(intent);
                    }
                }));
    }
}