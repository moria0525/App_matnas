package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManagerScreen extends AppCompatActivity {


    private FirebaseAuth auth;
    private StorageReference mStorage;
    String user;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private EditText name, date, des;
    private ListView actionsList;
    private ProgressDialog mProgressDialog;

    private static final int GALLERY_INTENT = 2;
    private boolean flag = false;
    private Context context;

    private Toolbar toolbar;
    private TextView toolBarText;
    private String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_screen);
        context = getApplicationContext();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView)findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.actions_manager);

        actionsList = (ListView) findViewById(R.id.List);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProgressDialog = new ProgressDialog(this);


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
                                    int position, long id)
            {
                switch (position) {
                    case 0:
                      uploadPictureTogallery();
                        break;
                    case 1:
                      addNewActivity();
                        break;
                }
            }

        });
    }

    private void addNewActivity()
    {
        Intent i = new Intent(this,AddActivity.class);
        startActivity(i);

//        //Store data in database
//        DatabaseReference usersRef = mDatabase.child("Users");
//        Map<String, String> userData = new HashMap<String, String>();
//
//        String str1 = "a";
//        String str2 = "b";
//        String str3 = "c";
//        String str4 = "e";
//
//        userData.put("Nombre", str1);
//        userData.put("Password", str2);
//        userData.put("Confirmed", str3);
//        userData.put("Email",str4);
//
//
//        mDatabase.child("Users").setValue(userData);

        //setContentView(R.layout.addnewactivity);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("הוספת חוג חדש");
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        final View layoutView = inflater.inflate(R.layout.addnewactivity, null);
//        builder.setView(layoutView);
//        builder.setPositiveButton("אישור",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {}
//                });
//        builder.setNegativeButton("ביטול",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {}
//                });
//        final AlertDialog dialog = builder.create();
//        dialog.show();
    }

    private void uploadPictureTogallery() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("העלאת תמונות לגלרייה");
        LayoutInflater inflater = LayoutInflater.from(this);

        final View layoutView = inflater.inflate(R.layout.muploadimage, null);
        builder.setView(layoutView);


        builder.setPositiveButton("אישור",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        builder.setNegativeButton("ביטול",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean entriesValid = true;
                try {
                    name = (EditText) layoutView.findViewById(R.id.et_name_event);
                    date = (EditText) layoutView.findViewById(R.id.et_date_event);
                    des = (EditText) layoutView.findViewById(R.id.et_description_event);

                    String s_event = name.getText().toString();
                    String s_date = date.getText().toString();
                    String s_des = des.getText().toString();
                    if (TextUtils.isEmpty(s_event)) {
                        name.setError("insert name");
                        entriesValid = false;

                    } else if (TextUtils.isEmpty(s_date)) {
                        date.setError("insert date");
                        entriesValid = false;
                    } else if (TextUtils.isEmpty(s_des)) {
                        des.setError("insert description");
                        entriesValid = false;
                    } else if (!flag) {
                        Toast.makeText(context, "לא נבחרו תמונות להעלאה", Toast.LENGTH_LONG).show();
                        entriesValid = false;
                    }

                } catch (Exception e) {
                    entriesValid = false;
                }

                if (entriesValid) {
                    dialog.dismiss();
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    public boolean onClickUploadPictures(View v) {
        flag = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // startActivityForResult(intent,GALLERY_INTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Upload Done", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    flag = true;
                }
            });
        } else {
            flag = false;
        }
    }
}