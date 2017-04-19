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
    private EditText name, date, des;
    private ListView actionsList;
    private ProgressDialog mProgressDialog;

    private static final int GALLERY_INTENT = 2;
    private boolean flag = false;
    private Context context;

    private Toolbar toolbar;
    private TextView toolBarText;
    private String[] values;


    int counter = 0;
    private TextView count;
    private View layoutView;

    /*
    code from file
     */

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri imgUri;


    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;
    String s_event, s_date, s_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_screen);
        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.actions_manager);


        actionsList = (ListView) findViewById(R.id.List);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


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
                                    int position, long id) {
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

    private void addNewActivity() {
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    private void uploadPictureTogallery() {

        final AlertDialog.Builder builder;
        final AlertDialog dialog;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("העלאת תמונות לגלרייה");
        LayoutInflater inflater = LayoutInflater.from(this);

        layoutView = inflater.inflate(R.layout.muploadimage, null);
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
                        counter = 0;
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        dialog = builder.create();
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
                    // count = (TextView) layoutView.findViewById(R.id.cunt);

                    s_event = name.getText().toString();
                    s_date = date.getText().toString();
                    s_des = des.getText().toString();
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
                    btnUpload_Click();
                    dialog.dismiss();
                    counter = 0;
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                counter = 0;
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
        return flag;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            counter++;
            count = (TextView) layoutView.findViewById(R.id.counter);
            count.setText("נבחרו עד כה: " + counter + " תמונות");
            mProgressDialog.dismiss();
            flag = true;
        } else {
            flag = false;
        }
    }


    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click() {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading image");
            dialog.show();

            //Get the storage reference
            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //Dimiss dialog when success
                    dialog.dismiss();
                    //Display success toast msg
                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    ImageUpload imageUpload = new ImageUpload(s_event, s_date, s_des, taskSnapshot.getDownloadUrl().toString());

                    //Save image info in to firebase database
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(imageUpload);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Dimiss dialog when error
                            dialog.dismiss();
                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Show upload progress

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            mProgressDialog.setMessage("Uploading...");
//            mProgressDialog.show();
//            Uri uri = data.getData();
//
//            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
//            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getApplicationContext(), "Upload Done", Toast.LENGTH_LONG).show();
//                    counter++;
//                    count = (TextView) layoutView.findViewById(R.id.counter);
//                    count.setText("נבחרו עד כה: " + counter + " תמונות");
//                    mProgressDialog.dismiss();
//
//                    flag = true;
//                }
//            });
//        } else {
//            flag = false;
//        }
//    }
}


//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ManagerScreen extends AppCompatActivity {
//
//
//    private FirebaseAuth auth;
//    private StorageReference mStorage;
//    String user;
//    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//    private EditText name, date, des;
//    private ListView actionsList;
//    private ProgressDialog mProgressDialog;
//
//    private static final int GALLERY_INTENT = 2;
//    private boolean flag = false;
//    private Context context;
//
//    private Toolbar toolbar;
//    private TextView toolBarText;
//    private String[] values;
//
//
//    int counter = 0;
//    private TextView count;
//    private View layoutView;
//
//    /*
//    code from file
//     */
//
//    private Uri imgUri;
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manager_screen);
//        context = getApplicationContext();
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolBarText = (TextView) findViewById(R.id.toolBarText);
//        toolBarText.setText(R.string.actions_manager);
//
//
//        actionsList = (ListView) findViewById(R.id.List);
//        //Get Firebase auth instance
//        auth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance().getReference();
//        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        mProgressDialog = new ProgressDialog(this);
//
//
//        // Defined Array values to show in ListView
//        values = getResources().getStringArray(R.array.actions);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);
//
//
//        // Assign adapter to ListView
//        actionsList.setAdapter(adapter);
//
//        // ListView Item Click Listener
//        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                switch (position) {
//                    case 0:
//                        uploadPictureTogallery();
//                        break;
//                    case 1:
//                        addNewActivity();
//                        break;
//                }
//            }
//
//        });
//    }
//
//    private void addNewActivity() {
//        Intent i = new Intent(this, AddActivity.class);
//        startActivity(i);
//    }
//
//    private void uploadPictureTogallery() {
//
//
//        final AlertDialog.Builder builder;
//        final AlertDialog dialog;
//
//        builder = new AlertDialog.Builder(this);
//        builder.setTitle("העלאת תמונות לגלרייה");
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        layoutView = inflater.inflate(R.layout.muploadimage, null);
//        builder.setView(layoutView);
//
//
//        builder.setPositiveButton("אישור",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Do nothing here because we override this button later to change the close behaviour.
//                        //However, we still need this because on older versions of Android unless we
//                        //pass a handler the button doesn't get instantiated
//                    }
//                });
//        builder.setNegativeButton("ביטול",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Do nothing here because we override this button later to change the close behaviour.
//                        //However, we still need this because on older versions of Android unless we
//                        //pass a handler the button doesn't get instantiated
//                    }
//                });
//        dialog = builder.create();
//        dialog.show();
//        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean entriesValid = true;
//                try {
//                    name = (EditText) layoutView.findViewById(R.id.et_name_event);
//                    date = (EditText) layoutView.findViewById(R.id.et_date_event);
//                    des = (EditText) layoutView.findViewById(R.id.et_description_event);
//                    // count = (TextView) layoutView.findViewById(R.id.cunt);
//
//                    String s_event = name.getText().toString();
//                    String s_date = date.getText().toString();
//                    String s_des = des.getText().toString();
//                    if (TextUtils.isEmpty(s_event)) {
//                        name.setError("insert name");
//                        entriesValid = false;
//
//                    } else if (TextUtils.isEmpty(s_date)) {
//                        date.setError("insert date");
//                        entriesValid = false;
//                    } else if (TextUtils.isEmpty(s_des)) {
//                        des.setError("insert description");
//                        entriesValid = false;
//                    } else if (!flag) {
//                        Toast.makeText(context, "לא נבחרו תמונות להעלאה", Toast.LENGTH_LONG).show();
//                        entriesValid = false;
//                    }
//
//                } catch (Exception e) {
//                    entriesValid = false;
//                }
//
//                if (entriesValid) {
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_manager, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_logout:
//                auth.signOut();
//                startActivity(new Intent(ManagerScreen.this, activity_main.class));
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//
//    }
//
//    public boolean onClickUploadPictures(View v) {
//        flag = true;
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        // startActivityForResult(intent,GALLERY_INTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT);
//        return flag;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            mProgressDialog.setMessage("Uploading...");
//            mProgressDialog.show();
//            Uri uri = data.getData();
//
//            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
//            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getApplicationContext(), "Upload Done", Toast.LENGTH_LONG).show();
//                    counter++;
//                    count = (TextView) layoutView.findViewById(R.id.counter);
//                    count.setText("נבחרו עד כה: " + counter + " תמונות");
//                    mProgressDialog.dismiss();
//
//                    flag = true;
//                }
//            });
//        } else {
//            flag = false;
//        }
//    }
//}