package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.user.app_matnas.FirebaseHelper.*;


/*
 * This Activity represents an screen categories of business of app and
 * Allows navigation to all business in select category
 */

public class activity_category extends AppCompatActivity {

    private ArrayList<String> categoryList;
    private AdapterString adapter;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;
    private Button button;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        //find if id's fields and init variables
        gv = (GridView) findViewById(R.id.gallery_grid);
        gv.setNumColumns(3);
        gv.setPadding(15, 10, 15, 10);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_business);
        button = (Button) findViewById(R.id.btn_Bregister);

        button.setVisibility(View.VISIBLE);
        categoryList = new ArrayList<>();

        showProgressDialog();
        mDatabaseRef.child(DB_BUSINESS).addValueEventListener(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                                      //Fetch category data from firebase database
                                                                      for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                          categoryList.add(snapshot.getKey());
                                                                      }
                                                                      hideProgressDialog();
                                                                      //Init adapter
                                                                      adapter = new AdapterString(activity_category.this, R.layout.category, categoryList, 0);
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
                //Pass the category to activity_business
                Intent intent = new Intent(activity_category.this, activity_business.class);
                intent.putExtra("category", item);
                //Start activity
                startActivity(intent);
            }
        });
    }

    //This method open form dialog to register as new business
    public void onClickBRegister(View view) {
        LayoutInflater inflater = LayoutInflater.from(activity_category.this);
        Register r = new Register(getString(R.string.title_register_business), activity_category.this);
        r.showDialog(inflater);
    }


    //This method show progress dialog until loading all data
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_category.this);
            mProgressDialog.setMessage(getString(R.string.loadingCategories));
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
