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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class activity_business extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    public ArrayList<String> categoryList;
    private StringAdapter adapter;
    private ProgressDialog progressDialog;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums);
        categoryList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_events);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("המתן לטעינת הקטגוריות");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryList.add(snapshot.getKey());
                }

                //Init adapter
                adapter = new StringAdapter(activity_business.this, R.layout.gallery_item, categoryList);
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
                String item = (String) parent.getItemAtPosition(position);
                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(activity_business.this, Esek.class);
                intent.putExtra("category", item);
                //Start details activity
                startActivity(intent);
            }
        });
    }

}
