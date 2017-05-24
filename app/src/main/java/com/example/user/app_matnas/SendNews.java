package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;

public class SendNews extends AppCompatActivity {
    private EditText content;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ImageView image;
    private RadioGroup radioGroup;
    private RadioButton rb_send, rb_notSend;
    private Button btn_selectImage;
    public static final int GALLERY_CODE = 1;
    private boolean flagSend = false;
    private Uri imgUri;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    public static String FB_STORAGE = "news";
    String date;
    String s_content;

    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("שליחת הודעה למשתמשים");

        content = (EditText) findViewById(R.id.et_content_news);
        btn_selectImage = (Button) findViewById(R.id.btn_selectImage);
        image = (ImageView) findViewById(R.id.imageNews);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_send = (RadioButton) findViewById(R.id.rb_send);
        rb_notSend = (RadioButton) findViewById(R.id.rb_notSend);

        mDatabase = FirebaseDatabase.getInstance().getReference("news");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rb_send) {
                    flagSend = true;
                } else if (checkedId == R.id.rb_notSend) {
                    flagSend = false;
                }

            }
        });
    }

    public void onClickNewsImage(View view) {
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
                 selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(SendNews.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(SendNews.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
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
                saveFields();
                break;
            case R.id.action_cancel:
                openMessage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @SuppressWarnings("VisibleForTests")
    private void saveFields() {
        date = DateFormat.getDateTimeInstance().format(new Date());
        s_content = content.getText().toString();


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("שולח את ההודעה");
            dialog.show();
                //Get the storage reference
                StorageReference ref = mStorageRef.child(FB_STORAGE).child(date);

                //TODO without image...
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //add data to DB
                        News news = new News(date, s_content, taskSnapshot.getDownloadUrl().toString(), flagSend);
                        try {
                            mDatabase.child(date).setValue(news);
                        } catch (DatabaseException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                        backToManagerScreen();
                        //if(flagSend)
                      //  {
                            Notification();
                       // }

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
                                dialog.setMessage((int) progress + "%");
                            }
                        });
        }
//   //  else {
//     //   Toast.makeText(getApplicationContext(), "שגיאה!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
//    }
//}

    private void Notification()
    {

        mDatabase.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                String[] tmp = new String[length];
                while (i < length) {
                    tmp[i] = iterator.next().getValue().toString();
                    i++;
                }

        // notification is selected
        Intent intent = new Intent(SendNews.this, activity_messages.class);
        PendingIntent pIntent = PendingIntent.getActivity(SendNews.this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = null;
                Bitmap bm = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                {
                    bm = getBitmapfromUrl(tmp[2]);

                    noti = new Notification.Builder(getApplicationContext())
                    .setContentTitle(tmp[0])
                    .setContentText(tmp[1]).setSmallIcon(R.mipmap.logo)
                    .setContentIntent(pIntent)
                    .setStyle(new Notification.BigPictureStyle()
                    .bigPicture(bm)).build();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });



    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

            /*Method to see message if click cancel without saving*/
    public void openMessage() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancelMessage));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
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

    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        Intent i = new Intent(SendNews.this, ManagerScreen.class);
        startActivity(i);

    }
}



