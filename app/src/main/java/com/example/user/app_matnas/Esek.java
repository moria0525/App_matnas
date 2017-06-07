package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import java.util.List;

public class Esek extends AppCompatActivity {


    private EsekAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Business> esekList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;
    String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Intent i = getIntent();
        category = i.getStringExtra("category");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(category);

        esekList = new ArrayList<>();

        list = (ListView) findViewById(R.id.list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("business");

        getDataFromDB();
    }

    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    esekList.add(snapshot.getValue(Business.class));
                }
                hideProgressDialog();
                adapter = new EsekAdapter(Esek.this, R.layout.activity_esek, esekList);
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
            mProgressDialog = new ProgressDialog(Esek.this);
            mProgressDialog.setMessage("טוען את העסקים..עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}


//            @Override
//            public boolean onCreateOptionsMenu(Menu menu) {
//
//                MenuInflater inflater = getMenuInflater();
//                inflater.inflate(R.menu.menu_search, menu);
//                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
//                SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//                searchView.setQueryHint("חיפוש חופשי");
//
//                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//
//
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//
//                        searchView.clearFocus();
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        newText = newText.toLowerCase();
//                        ArrayList<Project> newList = new ArrayList<>();
//                        for (Project project : projectList) {
//                            String name = project.getProjectName().toLowerCase();
//                            String des = project.getProjectDes().toLowerCase();
//                            if (name.contains(newText) || des.contains(newText)) {
//                                newList.add(project);
//                            }
//                        }
//                        adapter.setFilter(newList);
//
//                        return true;
//                    }
//                };
//                searchView.setOnQueryTextListener(queryTextListener);
//                return true;
//            }
//
//        }
//

