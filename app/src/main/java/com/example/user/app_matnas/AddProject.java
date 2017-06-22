package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.user.app_matnas.FirebaseHelper.*;


/*
 * This Activity to add new Project to app
 */


public class AddProject extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, description;
    private ImageView logo;
    private Project p_edit;
    private Project project;
    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        //init screen and variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[5]);

        //find id fields
        name = (EditText) findViewById(R.id.et_name_project);
        description = (EditText) findViewById(R.id.et_des_project);
        logo = (ImageView) findViewById(R.id.imageProject);

        p_edit = (Project) getIntent().getSerializableExtra("edit");

        if (p_edit != null) {
            toolBarText.setText(getResources().getStringArray(R.array.actions)[6]);
            fillFields();
        }
    }

    private void fillFields() {
        name.setText(p_edit.getProjectName());
        description.setText(p_edit.getProjectDes());
        Glide.with(getApplicationContext()).load(p_edit.getProjectLogo()).into(logo);

    }

    /*Method to create menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveDB();
                break;
            case R.id.action_cancel:
                openMessage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    /*Method to see message if click cancel without saving*/
    public void openMessage() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancelProject));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void onClickProjectLogo(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectImage)), GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                imgUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                logo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddProject.this, R.string.error, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AddProject.this, R.string.logoProject, Toast.LENGTH_LONG).show();
        }
    }


    /*Method to validation fields in form */
    @SuppressWarnings("VisibleForTests")
    private void saveDB() {

        boolean flag = true;
        final String s_name = name.getText().toString();
        final String s_description = description.getText().toString();

        if (s_name.isEmpty()) {
            name.setError("");
            flag = false;
        }
        if (s_description.isEmpty()) {
            description.setError("");
            flag = false;
        }
        if (p_edit == null && imgUri == null) {
            Toast.makeText(getApplicationContext(), R.string.logoProject, Toast.LENGTH_LONG).show();
            flag = false;
        }

        if (flag) {
            //Get the storage reference
            StorageReference ref = mStorageRef.child(ST_STORAGE_PROJECT).child(s_name);
            if (p_edit != null) {
                project = new Project(s_name, s_description, p_edit.getProjectLogo());
                try {
                    mDatabaseRef.child(DB_PROJECTS).child(s_name).setValue(project);
                } catch (DatabaseException e) {
                    Toast.makeText(getApplicationContext(), R.string.error + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), R.string.successUpdateProject, Toast.LENGTH_LONG).show();
                finish();
            } else {
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //add data to DB
                        project = new Project(s_name, s_description, taskSnapshot.getDownloadUrl().toString());
                        try {
                            mDatabaseRef.child(DB_PROJECTS).child(s_name).setValue(project);
                        } catch (DatabaseException e) {
                            Toast.makeText(getApplicationContext(), R.string.error + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), R.string.successAddProject, Toast.LENGTH_LONG).show();
                        finish();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //Display err toast msg
                                Toast.makeText(getApplicationContext(), R.string.error + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }

}