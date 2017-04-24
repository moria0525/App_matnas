package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.R.attr.imeActionId;


public class activity_gallery extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    public List<Image> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        imgList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_gallery);
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UploadImage.FB_DATABASE_PATH);

        Toast.makeText(getApplicationContext(), "ImageListActivity1", Toast.LENGTH_LONG).show();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   progressDialog.dismiss();
                                                   Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_LONG).show();
                                                   //Fetch image data from firebase database
                                                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                       //ImageUpload class require default constructor
                                                       Image img = snapshot.getValue(Image.class);
                                                       imgList.add(img);
                                                   }

                                                   //Init adapter
                                                   adapter = new ImageListAdapter(activity_gallery.this, R.layout.image_item, imgList);
                                                   Toast.makeText(getApplicationContext(), "adapter", Toast.LENGTH_LONG).show();
                                                   //Set adapter for listview
                                                   gv.setAdapter(adapter);
                                               }

                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {
                                                   progressDialog.dismiss();
                                               }
                                           }
        );

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Image item = (Image) parent.getItemAtPosition(position);
                Image item1 = (Image) parent.getItemAtPosition(position + 1);
                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(activity_gallery.this, FullImageGallery.class);
                intent.putExtra("image", item.getUrl());
                intent.putExtra("image1", item1.getUrl());
                //Start details activity
                startActivity(intent);
            }

        });
    }

}