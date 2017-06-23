package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity represents an screen of categories of business in app
 * Allows to manager enter want category
 */


public class SelectCategory extends AppCompatActivity {

    private ArrayList<String> categoryList;
    private ProgressDialog mProgressDialog;
    private ListView listView;
    private Toolbar toolbar;
    private TextView toolBarText;
    private Context context;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //find if id's fields and init variables
        context = getApplicationContext();
        categoryList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.editCategory);


        //Show progress dialog during list category loading
        showProgressDialog();

        mDatabaseRef.child(DB_BUSINESS).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                           @Override
                                                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                                                               //Fetch image data from firebase database
                                                                               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                   categoryList.add(snapshot.getKey());
                                                                               }
                                                                               if (categoryList.size() == 0) {
                                                                                   Toast.makeText(context, R.string.noBusiness, Toast.LENGTH_LONG).show();
                                                                                   finish();
                                                                                   return;
                                                                               }
                                                                               hideProgressDialog();
                                                                               //Init adapter
                                                                               adapter = new ArrayAdapter<String>(SelectCategory.this, android.R.layout.simple_list_item_1, android.R.id.text1, categoryList);
                                                                               // Assign adapter to ListView
                                                                               listView.setAdapter(adapter);
                                                                           }

                                                                           @Override
                                                                           public void onCancelled(DatabaseError databaseError) {
                                                                               hideProgressDialog();
                                                                           }
                                                                       }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String category = (String) parent.getItemAtPosition(position);
                int active = getIntent().getIntExtra("active", -1);
                EditBusiness b = new EditBusiness(SelectCategory.this);
                b.getDB(category, active);
            }
        });
    }

    //This method show progress dialog until loading all data
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(SelectCategory.this);
            mProgressDialog.setMessage(getString(R.string.loading));
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