package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class activity_project extends AppCompatActivity {
    private ProjectAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Project> projectList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;
    private View layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("פרוייקטים");
        list = (ListView) findViewById(R.id.list);
        getDataFromDB();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final Project project = projectList.get(position);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity_project.this);
            alertDialogBuilder.setTitle(project.getProjectName());
            alertDialogBuilder.setMessage(project.getProjectDes())
                    .setPositiveButton(R.string.joinProject, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                            LayoutInflater inflater = LayoutInflater.from(activity_project.this);
                            register r = new register(project.getProjectName(),activity_project.this);
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

    private void show()
    {
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity_project.this);

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.activity_register, null);
//        dialogBuilder.setView(dialogView);
//
//        EditText editText = (EditText) dialogView.findViewById(R.id.register_email);
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();



//        alertDialogBuilder.setMessage("second");
//        alertDialogBuilder.create();
//        alertDialogBuilder.show();

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






