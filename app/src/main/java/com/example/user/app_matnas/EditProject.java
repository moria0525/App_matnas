package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static com.example.user.app_matnas.FirebaseHelper.*;

public class EditProject extends AppCompatActivity {
    public List<String> proList;
    String name;
    private ListView listView;
    private Toolbar toolbar;
    private TextView toolBarText;
    Context context;
    String flag;
    ArrayAdapter<String> adapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        flag = getIntent().getStringExtra("action");

        context = getApplicationContext();
        proList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("בחר פרויקט");

        showProgressDialog();
        getDB();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Project project;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                final String select = (String) parent.getItemAtPosition(position);
                final DatabaseReference child = mDatabaseRef.child(DB_PROJECTS).child(select);
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot)
                    {
                        project = snapshot.getValue(Project.class);
                        if (flag.equals("edit"))
                        {
                            finish();
                            Intent i = new Intent(EditProject.this, AddProject.class);
                            i.putExtra("edit", project);
                            startActivity(i);
                            finish();
                        }
                        else if (flag.equals("delete"))
                        {
                            finish();
                            child.removeValue();
                            mStorageRef.child(ST_STORAGE_LOGO).child(select).delete();
                            Toast.makeText(context, "הפרויקט נמחק בהצלחה", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

            }
        });
    }

    public void getDB() {

        mDatabaseRef.child(DB_PROJECTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String project = snapshot.getValue(Project.class).getProjectName();
                    proList.add(project);
                }
                hideProgressDialog();
                if (proList.size() == 0) {
                    Toast.makeText(context, "לא נמצאו פרויקטים", Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new ArrayAdapter<String>(EditProject.this, android.R.layout.simple_list_item_1, android.R.id.text1, proList);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
                                                                }
        );

    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(EditProject.this);
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
}