package com.example.user.app_matnas;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManagerScreen extends AppCompatActivity {

    private FirebaseAuth auth;
    String user;
    private ListView actionsList;
    private Toolbar toolbar;
    private TextView toolBarText;
    private String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.actions_manager);

        actionsList = (ListView) findViewById(R.id.list);

        auth = FirebaseAuth.getInstance();

        // Defined Array values to show in ListView
        values = getResources().getStringArray(R.array.actions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        actionsList.setAdapter(adapter);

        // ListView Item Click Listener
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        uploadPictureTogallery();
                        break;
                    case 1:
                        sendNews();
                        break;
                    case 2:
                        addNewActivity();
                        break;
                    case 3:
                        editActivity();
                        break;
                    case 4:
                        deleteActivity();
                        break;
                    case 5:
                        addNewProject();
                        break;
                    case 6:
                        editProject();
                        break;
                    case 7:
                        deleteProject();
                        break;
                    case 8:
                        AddNewWorkshop();
                        break;
                    case 9:
                        editWorkShop();
                        break;
                    case 10:
                        deleteWorkShop();
                        break;
                    case 14:
                        AddNewTeam();
                        break;
                    case 15:
                        editTeam();
                        break;
                    case 16:
                        deleteTeam();
                        break;
                }
            }

        });
    }


    private void uploadPictureTogallery() {

        Intent i = new Intent(this, UploadImage.class);
        startActivity(i);
    }

    private void sendNews()
    {
        Intent i = new Intent(this, SendNews.class);
        startActivity(i);
    }

    private void addNewActivity() {
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    private void editActivity() {
        LayoutInflater inflater = LayoutInflater.from(ManagerScreen.this);
        EditActivity ea = new EditActivity(ManagerScreen.this);
        ea.getDB(-1);
    }

    private void deleteActivity() {
        LayoutInflater inflater = LayoutInflater.from(ManagerScreen.this);
        EditActivity ea = new EditActivity(ManagerScreen.this);
        ea.getDB(0);
    }

    private void addNewProject() {
        Intent i = new Intent(this, AddProject.class);
        startActivity(i);
    }
    private void editProject() {
        EditProject ep = new EditProject(ManagerScreen.this);
        ep.getDB(-1);
    }
    private void deleteProject() {
        EditProject ep = new EditProject(ManagerScreen.this);
        ep.getDB(0);
    }
    private void AddNewTeam()
    {
        Intent i = new Intent(this, AddTeam.class);
        startActivity(i);
    }
    private void editTeam() {
        EditTeam t = new EditTeam(ManagerScreen.this);
        t.getDB(-1);
    }
    private void deleteTeam() {
        EditTeam t = new EditTeam(ManagerScreen.this);
        t.getDB(0);
    }
    private void AddNewWorkshop()
    {
        Intent i = new Intent(this, AddWorkShop.class);
        startActivity(i);
    }
    private void editWorkShop() {
        EditWorkShop ws = new EditWorkShop(ManagerScreen.this);
        ws.getDB(-1);
    }
    private void deleteWorkShop() {
        EditWorkShop ws = new EditWorkShop(ManagerScreen.this);
        ws.getDB(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                auth.signOut();
                startActivity(new Intent(ManagerScreen.this, activity_main.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
}