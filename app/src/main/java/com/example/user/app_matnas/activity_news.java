package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.user.app_matnas.MainAdapter.countNotification;
import static com.example.user.app_matnas.MainAdapter.notification;


public class activity_news extends AppCompatActivity {
    private NewsAdapter adapter;
    private DatabaseReference databaseReference;
    private List<News> newsList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        countNotification = 0;
        notification.setText("" + countNotification);
        if (countNotification <= 0) {
            notification.setVisibility(View.INVISIBLE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_news);
        list = (ListView) findViewById(R.id.list);

        getDataFromDB();

    }


    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        News news = postSnapShot.getValue(News.class);
                        newsList.add(news);
                    }
                }
                Collections.reverse(newsList);
                hideProgressDialog();
                adapter = new NewsAdapter(activity_news.this, R.layout.activity_news, newsList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_news.this);
            mProgressDialog.setMessage("עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("חיפוש חופשי");


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

