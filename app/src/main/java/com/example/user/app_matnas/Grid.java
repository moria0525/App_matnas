package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Iterator;


public class Grid extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    public static ArrayList<String> urlList;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    private GridView gv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);
        Intent i = getIntent();
        String image = i.getStringExtra("grid");
       // Toast.makeText(getApplicationContext(), image, Toast.LENGTH_LONG).show();
        urlList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UploadImage.FB_DATABASE_PATH + "/");

        Toast.makeText(getApplicationContext(), "path\n" + mDatabaseRef.child(image).orderByChild("url").equalTo("url").toString(), Toast.LENGTH_LONG).show();

        mDatabaseRef.child(image).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                int i = 0;
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                String[] sampleString = new String[length];
                while(i < length) {
                    sampleString[i] = iterator.next().getValue().toString();
                    urlList.add(sampleString[i]);
                    i++;
                    }
                adapter = new ImageListAdapter(Grid.this, R.layout.image_item, urlList);
                gv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
                                           }
        );
    }
}























//        // Toast.makeText(getApplicationContext(), "ImageListActivity1:\n"+ mDatabaseRef, Toast.LENGTH_LONG).show();
//
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//                                               @Override
//                                               public void onDataChange(DataSnapshot dataSnapshot) {
//                                                   progressDialog.dismiss();
//                                                   // Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_LONG).show();
//                                                   //Fetch image data from firebase database
//                                                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                       //ImageUpload class require default constructor
//                                                       Image img = snapshot.getValue(Image.class);
//                                                       imgList.add(img.getName());
//                                                   }
//
//                                                   //Init adapter
//                                                   adapter = new ImageListAdapter(com.example.user.app_matnas.activity_gallery.this, R.layout.gallery_item, imgList);
//                                                   //Toast.makeText(getApplicationContext(), "adapter", Toast.LENGTH_LONG).show();
//                                                   //Set adapter for listview
//                                                   gv.setAdapter(adapter);
//                                               }
//
//                                               @Override
//                                               public void onCancelled(DatabaseError databaseError) {
//                                                   progressDialog.dismiss();
//                                               }
//                                           }
//        );
//
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                //Get item at position
//                String item = (String) parent.getItemAtPosition(position);
//                //Pass the image title and url to DetailsActivity
//                Intent intent = new Intent(com.example.user.app_matnas.activity_gallery.this, Grid.class);
//                intent.putExtra("grid", item);
//                //Start details activity
//                startActivity(intent);
//            }
//
//        });
//    }




//package com.example.user.app_matnas;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Grid extends AppCompatActivity {
//
//    private DatabaseReference mDatabaseRef;
//    public List<String> imgList;
//    private ProgressDialog progressDialog;
//    private GridView gv;
//    private Toolbar toolbar;
//    private TextView toolBarText;
//    private ImageListAdapter adapter;
//    String str;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.images);
//        imgList = new ArrayList<>();
//        gv = (GridView) findViewById(R.id.gallery_grid);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        Intent i = getIntent();
//        str = i.getStringExtra("image");
//
//        toolBarText = (TextView) findViewById(R.id.toolBarText);
//        toolBarText.setText(str);
//        //Show progress dialog during list image loading
//
//        addImages();
//    }
//
//
//    private void addImages() {
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait loading list image...");
//        progressDialog.show();
//        progressDialog.dismiss();
//
//
//        mDatabaseRef.child("image").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                progressDialog.dismiss();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    Image img = snapshot.getValue();
//                    Toast.makeText(getApplicationContext(), "snapshot.getKey() \n " + img, Toast.LENGTH_LONG).show();
//
//                    //String u = img.getUrl();
//                    //Toast.makeText(getApplicationContext(), "data: \n " + u, Toast.LENGTH_LONG).show();
//                    //  names.add(img.toString());
//                }
//                adapter = new AdapterString(com.example.user.app_matnas.activity_gallery.this, R.layout.gallery_item, names);
//                gv.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "size :" + names.size(), Toast.LENGTH_LONG).show();
//
//
//                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                                              public void onItemClick(final AdapterView<?> parent, View v, final int position, long id) {
//                                                  //Get item at position
//                                                  String item = (String) parent.getItemAtPosition(position);
//                                                  Intent intent = new Intent(com.example.user.app_matnas.activity_gallery.this, FullImageGallery.class);
//                                                  intent.putExtra("image", item);
//
//                                                  //Start details activity
//                                                  startActivity(intent);
//                                              }
//                                          }
//                );
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                progressDialog.dismiss();
//            }
//
//
//        });
//
//    }
//}
//
//