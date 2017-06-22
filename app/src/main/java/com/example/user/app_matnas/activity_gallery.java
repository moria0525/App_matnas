
package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity represents an screen gallery of app
 * user can to enter each album in gallery
 */

public class activity_gallery extends AppCompatActivity {

    private ArrayList<String> imgList;
    private AdapterString adapter;
    private ProgressDialog mProgressDialog;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        imgList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_gallery);

        showProgressDialog();
        mDatabaseRef.child(DB_IMAGE).addValueEventListener
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         //Fetch image data from firebase database
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             imgList.add(snapshot.getKey());
                         }
                         hideProgressDialog();
                         //Init adapter
                         adapter = new AdapterString(activity_gallery.this, R.layout.gallery_item, imgList, -1);
                         //Set adapter for gridView
                         gv.setAdapter(adapter);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                         hideProgressDialog();
                     }
                 }
                );

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String item = (String) parent.getItemAtPosition(position);
                //Pass the album title to Album
                Intent intent = new Intent(activity_gallery.this, Album.class);
                intent.putExtra("category", item);
                //Start details activity
                startActivity(intent);
            }
        });
    }

    //This method show progress dialog until loading all data
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_gallery.this);
            mProgressDialog.setMessage(getString(R.string.loadingAlbums));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    //This method dismiss progress dialog if show after loading all data
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
