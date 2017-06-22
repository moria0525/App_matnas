package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity represents an screen project of app and
 * allows to register project, show info about project and more
 */

public class activity_project extends AppCompatActivity {
    private AdapterProject adapter;
    private List<Project> projectList;
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //find if id's fields and init variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_projects);
        list = (ListView) findViewById(R.id.list);
        projectList = new ArrayList<>();

        getDataFromDB();

        //show list project and open dialog to join selected project
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            final Project project = projectList.get(position);
                                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity_project.this);
                                            alertDialogBuilder.setTitle(project.getProjectName());
                                            alertDialogBuilder.setMessage(project.getProjectDes())
                                                    .setPositiveButton(R.string.joinProject, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                            LayoutInflater inflater = LayoutInflater.from(activity_project.this);
                                                            Register r = new Register(project.getProjectName(), activity_project.this);
                                                            r.showDialog(inflater);
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }
                                    }
        );
    }

    //This method get data of DB and set in list and list set in Adapter
    public void getDataFromDB() {

        showProgressDialog();
        mDatabaseRef.child(DB_PROJECTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Project project = postSnapShot.getValue(Project.class);
                        projectList.add(project);
                    }
                }
                hideProgressDialog();
                adapter = new AdapterProject(activity_project.this, R.layout.activity_project, projectList);
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
            mProgressDialog = new ProgressDialog(activity_project.this);
            mProgressDialog.setMessage(getString(R.string.loadingProject));
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
                ArrayList<Project> newList = new ArrayList<>();
                for (Project project : projectList) {
                    String name = project.getProjectName().toLowerCase();
                    String des = project.getProjectDes().toLowerCase();
                    if (name.contains(newText) || des.contains(newText)) {
                        newList.add(project);
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