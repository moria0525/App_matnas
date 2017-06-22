package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*
 * This Activity of manager to upload images to gallery
 * Allows to images from gallery in device
 */

public class UploadImage extends AppCompatActivity {

    private EditText name;
    private ProgressDialog mProgressDialog;
    private boolean flag = false;
    private Context context;
    private int counter = 0;
    private TextView count;
    private View layoutView;
    private ArrayList<Uri> uris = null;
    private String FB_STORAGE_PATH;
    private String s_event;
    private DatabaseReference child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        child = mDatabaseRef.child(DB_IMAGE);

        mProgressDialog = new ProgressDialog(this);
        uploadPictureToGallery();
    }

    //This method to show dialog
    private void uploadPictureToGallery() {

        final AlertDialog.Builder builder;
        final AlertDialog dialog;

        builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getStringArray(R.array.actions)[0]);
        LayoutInflater inflater = LayoutInflater.from(this);

        layoutView = inflater.inflate(R.layout.upload_image, null);
        builder.setView(layoutView);


        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        builder.setNegativeButton(R.string.cancel,
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
                    s_event = name.getText().toString();
                    if (TextUtils.isEmpty(s_event)) {
                        name.setError("");
                        entriesValid = false;
                    } else if (!flag) {
                        Toast.makeText(context, R.string.errorSelectImgages, Toast.LENGTH_LONG).show();
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
                finish();
            }

        });

    }

    //This Method open gallery to select images from device to upload gallery screen
    public boolean onClickUploadPictures(View v) {

        flag = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectImage)), REQUEST_CODE);
        Toast.makeText(context, R.string.msgInfo, Toast.LENGTH_LONG).show();

        return flag;
    }

    //This method for treating selected images
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
                    count.setText(getString(R.string.select) + " " + counter + " " + getString(R.string.image));
                    mProgressDialog.dismiss();
                    flag = true;
                }
            }
        } else {
            flag = false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //This method to save images in Firebase Storage
    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click() {
        if (uris != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.upload) + " " + counter + " " + getString(R.string.selected));
            dialog.show();
            for (int i = 0; i < uris.size(); i++) {
                FB_STORAGE_PATH = s_event + "/";
                //Get the storage reference
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH).child(uris.get(i).getLastPathSegment());

                final int finalI = i;
                ref.putFile(uris.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Image imageUpload = new Image(s_event, taskSnapshot.getDownloadUrl().toString());

                        //Save image info in to firebase database
                        String uploadId = child.push().getKey();
                        child.child(s_event).child(uploadId).setValue(imageUpload.getUrl());
                        //Dimiss dialog when success
                        if (finalI == uris.size() - 1) {
                            dialog.dismiss();
                            //Display success toast msg
                            Toast.makeText(getApplicationContext(), R.string.successUpload, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //Dismiss dialog when error
                                dialog.dismiss();
                                //Display err toast msg
                                Toast.makeText(getApplicationContext(), R.string.error + " "+ e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                //Show upload progress
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage(getString(R.string.uploadImage) + " " + (int) progress + "%");
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.selectImages, Toast.LENGTH_LONG).show();
        }
    }
}
