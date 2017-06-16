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

import static com.example.user.app_matnas.FirebaseHelper.DB_WORKSHOP;
import static com.example.user.app_matnas.FirebaseHelper.mDatabaseRef;

public class ManagerScreen extends AppCompatActivity {

    private FirebaseAuth auth;
    String user;
    private ListView actionsList;
    private Toolbar toolbar;
    private TextView toolBarText;
    private String[] values;

    String edit = "edit";
    String delete = "delete";


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
                        Intent i = new Intent(ManagerScreen.this, UploadImage.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(ManagerScreen.this, SendNews.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(ManagerScreen.this, AddActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(ManagerScreen.this, EditActivity.class);
                        i.putExtra("action",edit);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(ManagerScreen.this, EditActivity.class);
                        i.putExtra("action",delete);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(ManagerScreen.this, AddProject.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(ManagerScreen.this, EditProject.class);
                        i.putExtra("action",edit);
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(ManagerScreen.this, EditProject.class);
                        i.putExtra("action",delete);
                        startActivity(i);
                        break;
                    case 8:
                        i = new Intent(ManagerScreen.this, AddWorkShop.class);
                        startActivity(i);
                        break;
                    case 9:
                        i = new Intent(ManagerScreen.this, EditWorkShop.class);
                        i.putExtra("action",edit);
//                        final DatabaseReference child = mDatabaseRef.child(DB_WORKSHOP);
//                            Toast.makeText(getApplicationContext(), child.getDatabase().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        break;
                    case 10:
                        i = new Intent(ManagerScreen.this, EditWorkShop.class);
                        i.putExtra("action",delete);
                        startActivity(i);
                        break;
                    case 11:
                        i = new Intent(ManagerScreen.this, AddTeam.class);
                        startActivity(i);
                        break;
                    case 12:
                        i = new Intent(ManagerScreen.this, EditTeam.class);
                        i.putExtra("action",edit);
                        startActivity(i);
                        break;
                    case 13:
                        i = new Intent(ManagerScreen.this, EditTeam.class);
                        i.putExtra("action",delete);
                        startActivity(i);
                        break;
                    case 14:
                        i = new Intent(ManagerScreen.this, AddBusiness.class);
                        startActivity(i);
                        break;
                    case 15:
                        i = new Intent(ManagerScreen.this, SelectCategory.class);
                        i.putExtra("active",1);
                        startActivity(i);
                        break;
                    case 16:
                        i = new Intent(ManagerScreen.this, SelectCategory.class);
                        i.putExtra("active",0);
                        startActivity(i);
                        break;
                }
            }

        });
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