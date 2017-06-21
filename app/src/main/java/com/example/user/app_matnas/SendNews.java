package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static com.example.user.app_matnas.MainAdapter.countNotification;
import static com.example.user.app_matnas.MainAdapter.notification;

public class SendNews extends AppCompatActivity {
    private EditText content;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ImageView image;
    private RadioGroup radioGroup;
    private RadioButton rb_send;
    public static final int GALLERY_CODE = 1;
    private boolean flagSend = false;
    private Uri imgUri;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    public static String FB_STORAGE = "news";
    private String date;
    private String s_content;
    boolean click = false;
    private Bitmap selectedImage;
    private String[] tmp;
    private News news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("הוספת חדשות");

        content = (EditText) findViewById(R.id.et_content_news);
        image = (ImageView) findViewById(R.id.imageNews);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_send = (RadioButton) findViewById(R.id.rb_send);
        tmp = new String[4];
        mDatabase = FirebaseDatabase.getInstance().getReference("news");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
        click = true;

        if (reqCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                imgUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imgUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(SendNews.this, "שגיאה", Toast.LENGTH_LONG).show();
            }

        } else {
            click = false;
            Toast.makeText(SendNews.this, "לא בחרת תמונה", Toast.LENGTH_LONG).show();
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

        boolean flag = true;
        s_content = content.getText().toString();
        if (TextUtils.isEmpty(s_content)) {
            flag = false;
            content.setError("");
        }
        if (radioGroup.getCheckedRadioButtonId() <= 0) {
            flag = false;
            rb_send.setError("");//Set error to last Radio button
        }

        if (flag) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            date = format.format(new Date());

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("מוסיף את החדשה");
            dialog.show();
            //Get the storage reference
            StorageReference ref = mStorageRef.child(FB_STORAGE).child(date);

            if (!click) {
                news = new News(date, s_content, "", flagSend);
                try {
                    mDatabase.child(date).setValue(news);
                } catch (DatabaseException e) {
                    Toast.makeText(getApplicationContext(), "שגיאה: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //add data to DB
                        news = new News(date, s_content, taskSnapshot.getDownloadUrl().toString(), flagSend);
                        try {
                            mDatabase.child(date).setValue(news);
                        } catch (DatabaseException e) {
                            Toast.makeText(getApplicationContext(), "שגיאה: " +e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //Dismiss dialog when error
                                dialog.dismiss();
                                //Display err toast msg
                                Toast.makeText(getApplicationContext(), "שגיאה: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            countNotification++;
            Toast.makeText(getApplicationContext(), "החדשה נוספה בהצלחה", Toast.LENGTH_LONG).show();
            //update count //TODO
            notification.setVisibility(View.VISIBLE);
            notification.setText("" + countNotification);
            if (flagSend) {

                Notification();
            }

            //case 1
            backToManagerScreen();
        }

    }


    private void Notification() {

        mDatabase.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                while (i < length) {
                    tmp[i] = iterator.next().getValue().toString();
                    i++;
                }
                new notiImage().execute();

                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


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
        finish();

    }


    //  AsyncTask
    private class notiImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = tmp[2];

            if (!TextUtils.isEmpty(imageURL)) {
                Bitmap bitmap = null;
                try {
                    // Download Image from URL
                    InputStream input = new java.net.URL(imageURL).openStream();
                    // Decode Bitmap
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Notification noti;
            Intent intent = new Intent(SendNews.this, Activity_news.class);
            PendingIntent pIntent = PendingIntent.getActivity(SendNews.this, (int) System.currentTimeMillis(), intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                if (!click) {

                    noti = new Notification.Builder(getApplicationContext())
                            .setContentTitle(tmp[0]).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .build();
                } else {
                    noti = new Notification.Builder(getApplicationContext())
                            .setContentTitle(tmp[0])
                            .setContentText(tmp[0]).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new Notification.BigPictureStyle()
                                    .bigPicture(result)).build();
                }
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, noti);

            }
        }
    }
}