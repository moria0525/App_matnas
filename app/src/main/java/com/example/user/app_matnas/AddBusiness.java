package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddBusiness extends AppCompatActivity{

    private static final String TAG = "google: ";
    private Context context;
    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, category, description, phone, mail;
    private ImageView image;
    private DatabaseReference mDatabase;
    private Business b_edit;
    private Business business;
    private Place _place;
    public static final int GALLERY_CODE = 1;
    private Uri imgUri;
    private StorageReference mStorageRef;
    public static String FB_STORAGE_BUSINESS = "business";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_business);

        //init screen and variables
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[14]);
        mDatabase = FirebaseDatabase.getInstance().getReference("business");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //find id fields
        name = (EditText) findViewById(R.id.et_name_bus);
        category = (EditText) findViewById(R.id.et_category_bus);
        description = (EditText) findViewById(R.id.et_des_bus);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

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


//        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//        spinner.setOnItemSelectedListener(this);
//
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final List<String> categories = new ArrayList<String>();
//                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.getKey();
//                    categories.add(areaName);
//                }
//
//
//                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(AddBusiness.this, android.R.layout.simple_spinner_item, categories);
//                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(Adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        b_edit = (Business) getIntent().getSerializableExtra("editBus");

        if (b_edit != null) {
            toolBarText.setText(getResources().getStringArray(R.array.actions)[15]);
            fillFields();
        }

    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        s_category = parent.getItemAtPosition(position).toString();
//    }
//    public void onNothingSelected(AdapterView<?> arg0) {
//
//    }




    private void fillFields() {
        name.setText(b_edit.getBusinessName());
        category.setText(b_edit.getBusinessCategory());
        description.setText(b_edit.getBusinessDes());
        phone.setText(b_edit.getBusinessPhone());
        mail.setText(b_edit.getBusinessMail());
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
                Toast.makeText(AddBusiness.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AddBusiness.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    /*Method to validation fields in form */
    @SuppressWarnings("VisibleForTests")
    private void saveDB() {

        final String s_name = name.getText().toString();
        final String s_description = description.getText().toString();
        final String s_category = category.getText().toString();
        final String s_phone = phone.getText().toString();
        final String s_mail = mail.getText().toString();
        final CharSequence s_address = _place.getAddress();

        //Get the storage reference
        StorageReference ref = mStorageRef.child(FB_STORAGE_BUSINESS).child(s_name);//CHILD?

        //TODO without image...

        if(b_edit!= null)
        {
            business = new Business(s_name, s_category, s_description, s_address, s_phone, s_mail, b_edit.getBusinessImage(),_place.getLatLng().latitude, _place.getLatLng().longitude);
            try {
                mDatabase.child(s_category).child(s_name).setValue(business);
            } catch (DatabaseException e) {
                Toast.makeText(getApplicationContext(), "220: "+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "פרטי בית העסק עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //add data to DB
                    business = new Business(s_name, s_category, s_description, s_address, s_phone, s_mail, taskSnapshot.getDownloadUrl().toString(),_place.getLatLng().latitude, _place.getLatLng().longitude);
                    try {
                        mDatabase.child(s_category).child(s_name).setValue(business);
                    } catch (DatabaseException e) {
                        Toast.makeText(getApplicationContext(), "236 "+e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "פרטי בית העסק נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),""+s_address,Toast.LENGTH_LONG).show();
                    finish();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), "249 "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        Intent i = new Intent(AddBusiness.this, ManagerScreen.class);
        startActivity(i);
    }
}