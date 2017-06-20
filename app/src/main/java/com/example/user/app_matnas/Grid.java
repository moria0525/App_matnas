package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Grid extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private ArrayList<String> urlList;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private Toolbar toolbar;
    private TextView toolBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);
        Intent i = getIntent();
        String image = i.getStringExtra("category");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(image);

        urlList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("המתן לטעינת התמונות...");
        progressDialog.show();


        //TODO progress dialog show
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UploadImage.FB_DATABASE_PATH + "/");

        mDatabaseRef.child(image).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                String[] url = new String[length];
                while (i < length) {
                    url[i] = iterator.next().getValue().toString();
                    urlList.add(url[i]);
                    i++;
                }

                mAdapter = new GalleryAdapter(Grid.this, urlList);
                mRecyclerView.setAdapter(mAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
                                                        }
        );

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(Grid.this, FullImage.class);
                        intent.putExtra("data", urlList);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));
    }
}
