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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.user.app_matnas.FirebaseHelper.*;


/*
 * This Activity to add new Business to app
 */

public class AddBusiness extends AppCompatActivity {

    private static final String TAG = "google: ";
    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, category, description, phone, mail;
    private ImageView image;
    private Business b_edit;
    private Business business;
    private Place _place;
    private Uri imgUri;
    private PlaceAutocompleteFragment autocompleteFragment;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_business);

        //init screen and variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[14]);
        name = (EditText) findViewById(R.id.et_name_bus);
        category = (EditText) findViewById(R.id.et_category_bus);
        description = (EditText) findViewById(R.id.et_des_bus);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint(getString(R.string.enterAddress));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                _place = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        phone = (EditText) findViewById(R.id.et_phone_bus);
        mail = (EditText) findViewById(R.id.et_email_bus);
        image = (ImageView) findViewById(R.id.imageBus);

        b_edit = (Business) getIntent().getSerializableExtra("editBusiness");

        if (b_edit != null) {
            toolBarText.setText(getResources().getStringArray(R.array.actions)[15]);
            fillFields();
        }

    }

    private void fillFields() {
        name.setText(b_edit.getBusinessName());
        category.setText(b_edit.getBusinessCategory());
        description.setText(b_edit.getBusinessDes());
        phone.setText(b_edit.getBusinessPhone());
        mail.setText(b_edit.getBusinessMail());
        autocompleteFragment.setHint("לשינוי כתובת העסק, לחץ כאן");
        latitude = b_edit.getLatitude();
        longitude = b_edit.getLongitude();
        Glide.with(getApplicationContext()).load(b_edit.getBusinessImage()).into(image);
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
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancelBusiness));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
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


    public void onClickBusImage(View view) {
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
                Toast.makeText(AddBusiness.this, R.string.error, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AddBusiness.this, R.string.logoBusiness, Toast.LENGTH_LONG).show();
        }
    }

    /*Method to validation fields in form */
    @SuppressWarnings("VisibleForTests")
    private void saveDB() {

        boolean flag = true;
        final String s_name = name.getText().toString();
        final String s_description = description.getText().toString();
        final String s_category = category.getText().toString();
        final String s_phone = phone.getText().toString();
        final String s_mail = mail.getText().toString();

        if (TextUtils.isEmpty(s_name)) {
            name.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_description)) {
            description.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_category)) {
            category.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_phone)) {
            phone.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_mail)) {
            mail.setError("");
            flag = false;
        }
        if (b_edit == null && imgUri == null) {
            Toast.makeText(getApplicationContext(), "לא בחרת לוגו של העסק", Toast.LENGTH_LONG).show();
            flag = false;
        }


        if (flag) {
            //Get the storage reference
            StorageReference ref = mStorageRef.child(ST_STORAGE_BUSINESS).child(s_name);//CHILD?

            if (b_edit != null) {
                business = new Business(s_name, s_category, s_description, s_phone, s_mail, b_edit.getBusinessImage(), this.latitude, longitude);
                try {
                    mDatabaseRef.child(DB_BUSINESS).child(s_category).child(s_name).setValue(business);
                } catch (DatabaseException e) {
                    Toast.makeText(getApplicationContext(), R.string.error + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), R.string.successUpdateBusiness, Toast.LENGTH_LONG).show();
                backToManagerScreen();
            } else {
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //add data to DB
                        business = new Business(s_name, s_category, s_description, s_phone, s_mail, taskSnapshot.getDownloadUrl().toString(), _place.getLatLng().latitude, _place.getLatLng().longitude);
                        try {
                            mDatabaseRef.child(DB_BUSINESS).child(s_category).child(s_name).setValue(business);
                        } catch (DatabaseException e) {
                            Toast.makeText(getApplicationContext(), R.string.error + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), R.string.successAddBusiness, Toast.LENGTH_LONG).show();
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


    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        finish();
        Intent i = new Intent(AddBusiness.this, ManagerScreen.class);
        startActivity(i);
    }
}