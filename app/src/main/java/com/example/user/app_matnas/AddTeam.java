package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddTeam extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, role, mail, description;
    private ImageView image;
    private DatabaseReference mDatabase;
    private Team t_edit;
    private Team team;
    public static final int GALLERY_CODE = 1;
    private Uri imgUri;
    private StorageReference mStorageRef;
    public static String FB_STORAGE_TEAM = "team";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        //init screen and variables
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[14]);
        mDatabase = FirebaseDatabase.getInstance().getReference("team");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //find id fields
        name = (EditText) findViewById(R.id.et_name_team);
        role = (EditText) findViewById(R.id.et_role_team);
        mail = (EditText) findViewById(R.id.et_mail_team);
        description = (EditText) findViewById(R.id.et_des_team);
        image = (ImageView) findViewById(R.id.imageTeam);

        t_edit = (Team) getIntent().getSerializableExtra("editTeam");

        if (t_edit != null) {
            toolBarText.setText(getResources().getStringArray(R.array.actions)[15]);
            fillFields();
        }




    }
    private void fillFields() {
        name.setText(t_edit.getTeamName());
        role.setText(t_edit.getTeamRole());
        mail.setText(t_edit.getTeamMail());
        description.setText(t_edit.getTeamDes());
        Glide.with(getApplicationContext()).load(t_edit.getTeamImage()).into(image);

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
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancelTeam));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
                        backToManagerScreen();
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


    public void onClickTeamImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (reqCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                imgUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddTeam.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AddTeam.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    /*Method to validation fields in form */
    @SuppressWarnings("VisibleForTests")
    private void saveDB() {

        final String s_name = name.getText().toString();
        final String s_role = role.getText().toString();
        final String s_mail = mail.getText().toString();
        final String s_description = description.getText().toString();

        //Get the storage reference
        StorageReference ref = mStorageRef.child(FB_STORAGE_TEAM).child(s_name);

        //TODO without image...

        if(t_edit!= null)
        {
            team = new Team(s_name, s_role, s_mail, s_description, t_edit.getTeamImage());
            try {
                mDatabase.child(s_name).setValue(team);
            } catch (DatabaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "פרטי איש הצוות עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //add data to DB
                    team = new Team(s_name, s_role, s_mail, s_description, taskSnapshot.getDownloadUrl().toString());
                    try {
                        mDatabase.child(s_name).setValue(team);
                    } catch (DatabaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "פרטי איש הצוות נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
                    finish();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        Intent i = new Intent(AddTeam.this, ManagerScreen.class);
        startActivity(i);
    }
}