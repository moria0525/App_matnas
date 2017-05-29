package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadImage extends AppCompatActivity {

    private EditText name;
    private ProgressDialog mProgressDialog;

    private boolean flag = false;
    private Context context;

    int counter = 0;
    private TextView count;
    private View layoutView;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri imgUri;
    ArrayList<Uri> uris = null;

    public static String FB_STORAGE_PATH;
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;
    String s_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        mProgressDialog = new ProgressDialog(this);
        uploadPictureTogallery();
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
                    // count = (TextView) layoutView.findViewById(R.id.cunt);

                    s_event = name.getText().toString();
                    if (TextUtils.isEmpty(s_event)) {
                        name.setError("insert name");
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
            public void onClick(View v) {
                counter = 0;
                dialog.dismiss();
                backToManagerScreen();
            }

        });

    }


    public boolean onClickUploadPictures(View v) {
//        flag = true;
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
        flag = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "בחר תמונות"), REQUEST_CODE);


        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    uris = new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        counter++;
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        uris.add(uri);
                    }

                }
                if (uris != null) {

                    count = (TextView) layoutView.findViewById(R.id.counter);
                    count.setText("נבחרו עד כה: " + counter + " תמונות");
                    mProgressDialog.dismiss();
                    flag = true;
                }
            }
        }
        else {
            flag = false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click() {
        if (uris != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("מעלה את "+counter+" התמונות שנבחרו");
            dialog.show();
            for(int i = 0;i<uris.size();i++)
            {
                FB_STORAGE_PATH = s_event + "/";
                //Get the storage reference
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH).child(uris.get(i).getLastPathSegment());

                final int finalI = i;
                ref.putFile(uris.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Image imageUpload = new Image(s_event, taskSnapshot.getDownloadUrl().toString());

                        //Save image info in to firebase database
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(s_event).child(uploadId).setValue(imageUpload.getUrl());
                        //Dimiss dialog when success
                        if(finalI == uris.size()-1)
                        {
                            dialog.dismiss();
                            //Display success toast msg
                            Toast.makeText(getApplicationContext(), "התמונות עלו בהצלחה", Toast.LENGTH_SHORT).show();
                            backToManagerScreen();
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //Dismiss dialog when error
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
                                dialog.setMessage("מעלה " + (int) progress + "%");
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
       finish();
    }

}
