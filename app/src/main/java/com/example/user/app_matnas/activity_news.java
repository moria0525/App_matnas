package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.user.app_matnas.AdapterMain.countNotification;
import static com.example.user.app_matnas.AdapterMain.notification;
import static com.example.user.app_matnas.FirebaseHelper.*;


/*
 * This Activity represents an screen news of app
 */

public class activity_news extends AppCompatActivity {
    private AdapterNews adapter;
    private List<News> newsList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //find if id's fields and init variables
        countNotification = 0;
        notification.setText("" + countNotification);
        if (countNotification <= 0) {
            notification.setVisibility(View.INVISIBLE);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_news);
        list = (ListView) findViewById(R.id.list);

        getDataFromDB();

    }


    //This method get data of DB and set in list and list set in Adapter
    public void getDataFromDB() {

        showProgressDialog();
        mDatabaseRef.child(DB_NEWS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        News news = postSnapShot.getValue(News.class);
                        newsList.add(news);
                    }
                }
                Collections.reverse(newsList); //to show the newest message up
                hideProgressDialog();
                adapter = new AdapterNews(activity_news.this, R.layout.activity_news, newsList);
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
            mProgressDialog = new ProgressDialog(activity_news.this);
            mProgressDialog.setMessage(getString(R.string.loadingNews));
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
                ArrayList<News> newList = new ArrayList<>();
                for (News news : newsList) {
                    String name = news.getNewsContent().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(news);
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

