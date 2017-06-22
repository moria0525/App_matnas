package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity represents an screen activities of app and
 * Allows navigation to all screens of app.
 */

public class activity_activities extends AppCompatActivity {

    private AdapterActivities adapter;
    private ProgressDialog mProgressDialog;
    private List<Activity> activityList;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);

        //find if id's fields and init variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_activities);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityList = new ArrayList<>();
        getDataFromDB();
    }

    //This method get data of DB and set in list and list set in Adapter
    public void getDataFromDB() {

        showProgressDialog();
        mDatabaseRef.child(DB_ACTIVITIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Activity activity = postSnapShot.getValue(Activity.class);
                        activityList.add(activity);
                    }
                }
                hideProgressDialog();
                adapter = new AdapterActivities(activityList, activity_activities.this);
                recyclerView.setAdapter(adapter);
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
            mProgressDialog = new ProgressDialog(activity_activities.this);
            mProgressDialog.setMessage(getString(R.string.loadingActivities));
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
                ArrayList<Activity> newList = new ArrayList<>();
                for (Activity activity : activityList) {
                    String name = activity.getActivityName().toLowerCase();
                    String age = activity.getActivityAge().toLowerCase();
                    String type = activity.getActivityType().toLowerCase();
                    String days = activity.getActivityDays().toLowerCase();
                    String des = activity.getActivityDes().toLowerCase();
                    String start = activity.getActivityStart().toLowerCase();
                    String end = activity.getActivityEnd().toLowerCase();

                    if (name.contains(newText) || age.contains(newText) || type.contains(newText)
                            || days.contains(newText) || des.contains(newText) || start.contains(newText)
                            || end.contains(newText)) {
                        newList.add(activity);
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