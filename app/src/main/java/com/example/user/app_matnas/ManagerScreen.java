package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;


public class ManagerScreen extends AppCompatActivity {

    private static final int PICK_IMAGE =2 ;
    private ListView actionsList;
    private FirebaseAuth auth;
    private EditText name, date, des;
    private ImageButton gallery;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_screen);

        actionsList = (ListView) findViewById(R.id.List);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        // Defined Array values to show in ListView
        String[] values = new String[]{"העלאת תמונות לגלרייה",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        actionsList.setAdapter(adapter);

        // ListView Item Click Listener
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                switch (itemPosition) {
                    case 0:
                        uploadPictureTogallery();

                }
            }

        });
    }

    public void uploadPictureTogallery() {

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
                    gallery = (ImageButton) layoutView.findViewById(R.id.gallery_event);

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
                    }
                    else if(!flag)
                    {
                        Toast.makeText(getApplicationContext(),"לא נבחרו תמונות להעלאה",Toast.LENGTH_LONG).show();
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

    public void onClickLogOut(View v) {
        auth.signOut();
        startActivity(new Intent(ManagerScreen.this, activity_main.class));
    }


    public boolean onClickUploadPictures(View v)
    {
        flag = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
       // startActivityForResult(intent,GALLERY_INTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT );
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK)
        {
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    flag = true;
                }
            });
        }
        else
        {
            flag = false;
        }
    }
}

