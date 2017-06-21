package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.ArrayList;

public class SelectCategory extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    public  ArrayList<String> categoryList;
    private ProgressDialog progressDialog;
    private ListView listView;
    private Toolbar toolbar;
    private TextView toolBarText;
    private Context context;
    ArrayAdapter<String> adapter;
    String active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        context = getApplicationContext();
        categoryList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.editCategory);


        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("עוד רגע..");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business");

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryList.add(snapshot.getKey());
                }
                if (categoryList.size() == 0) {
                    Toast.makeText(context, "לא נמצאו קטגוריות", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                progressDialog.dismiss();
                //Init adapter
                adapter = new ArrayAdapter<String>(SelectCategory.this, android.R.layout.simple_list_item_1, android.R.id.text1, categoryList);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
                                           }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String category = (String) parent.getItemAtPosition(position);
                int  active = getIntent().getIntExtra("active",-1);
                EditBusiness b = new EditBusiness(SelectCategory.this);
                b.getDB(category, active);
            }
        });
    }
}