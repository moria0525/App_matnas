package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_project extends AppCompatActivity {
    private ProjectAdapter adapter;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgressDialog;
    private List<Project> projectList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_project);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("פרוייקטים");
        list = (ListView) findViewById(R.id.list);
        //   list.setLayoutManager(new LinearLayoutManager(this));
        getDataFromDB();


    }


    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Project project = postSnapShot.getValue(Project.class);
                        projectList.add(project);
                    }
                }
                hideProgressDialog();
                adapter = new ProjectAdapter(activity_project.this, R.layout.activity_project, projectList);
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
            mProgressDialog = new ProgressDialog(activity_project.this);
            mProgressDialog.setMessage("טוען את הפרוייקטים..עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Project project = projectList.get(position);
                Intent intent = new Intent(activity_project.this, ShowProject.class);
                intent.putExtra("show", project);
                startActivity(intent);



            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        //searchView.setIconified(false);
        searchView.setQueryHint("חיפוש חופשי");


        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
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
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class ProjectAdapter extends ArrayAdapter<Project> {
        private android.app.Activity context;
        private int resource;
        private List<Project> listProjects;

        public ProjectAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<Project> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            listProjects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_PName);
            ImageView img = (ImageView) v.findViewById(R.id.imageViewProject);
            ImageView arrow = (ImageView) v.findViewById(R.id.arrow);
            arrow.setImageResource(R.drawable.next);
            tvName.setText(listProjects.get(position).getProjectName());
            Glide.with(context).load(listProjects.get(position).getProjectLogo()).into(img);
            return v;

        }

        @Override
        public int getCount() {
            return listProjects.size();
        }

        public void setFilter(ArrayList<Project> newList) {
            listProjects = new ArrayList<>();
            listProjects.addAll(newList);
            notifyDataSetChanged();

        }

    }

}






