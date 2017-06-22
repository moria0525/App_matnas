package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.user.app_matnas.FirebaseHelper.*;

import java.util.ArrayList;
import java.util.List;

/*
 * This Activity represents an screen business in app
 * Allows to call, send message and more of each business in list
 */

public class activity_business extends AppCompatActivity {

    private AdapterBusiness adapter;
    private List<Business> businessList;
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //get intent from activity_category.class
        Intent i = getIntent();
        category = i.getStringExtra("category");

        //find if id's fields and init variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(category);
        list = (ListView) findViewById(R.id.list);

        businessList = new ArrayList<>();
        getDataFromDB();
    }

    //This method get data of DB and set in list and list set in Adapter
    public void getDataFromDB() {

        showProgressDialog();
        mDatabaseRef.child(DB_BUSINESS).child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    businessList.add(snapshot.getValue(Business.class));
                }
                hideProgressDialog();
                adapter = new AdapterBusiness(activity_business.this, R.layout.activity_esek, businessList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();

            }
        });
    }

    //This method show progress dialog until loading all data
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_business.this);
            mProgressDialog.setMessage(getString(R.string.loadingBusiness));
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

    //This method create toolBar menu with 'search' action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.freeSearch));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Business> newList = new ArrayList<>();
                for (Business business : businessList) {
                    String name = business.getBusinessName().toLowerCase();
                    String des = business.getBusinessDes().toLowerCase();
                    if (name.contains(newText) || des.contains(newText)) {
                        newList.add(business);
                    }
                }
                adapter.setFilter(newList);

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }
}