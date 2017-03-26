package com.example.user.app_matnas;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class activity_events extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://app-matnas-22408.appspot.com/Photos/");
      //  storageReference.child("16169.jpg");
        List<FileDownloadTask> activeDownloadTasks = storageReference.getActiveDownloadTasks();

        Toast.makeText(getApplicationContext(), ""+ activeDownloadTasks.get(1),Toast.LENGTH_LONG).show();

        ImageView imageView = (ImageView)findViewById(R.id.imageView);


        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);
    }
}
