package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_business extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    public ArrayList<String> categoryList;
    private StringAdapter adapter;
    private ProgressDialog progressDialog;
    private GridView gv;
    private Toolbar toolbar;
    private TextView toolBarText;
    private Button button;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        context = getApplicationContext();
        categoryList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gallery_grid);

        gv.setNumColumns(3);
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.albums);
        gv.setPadding(15, 10, 15, 10);

//        ll.setBackgroundResource(R.color.colorBackgroundHome);
        //mRecyclerView.setHasFixedSize(true);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.text_business);

        button = (Button)findViewById(R.id.btn_Bregister);
        button.setVisibility(View.VISIBLE);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("עוד רגע..");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryList.add(snapshot.getKey());
                }

                //Init adapter
                adapter = new StringAdapter(Activity_business.this, R.layout.category, categoryList, 0);
                //Set adapter for listview
                gv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
                                           }
        );

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String item = (String) parent.getItemAtPosition(position);
                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(Activity_business.this, Esek.class);
                intent.putExtra("category", item);
                //Start details activity
                startActivity(intent);
            }
        });
    }

    public void onClickBRegister(View view)
    {
        LayoutInflater inflater = LayoutInflater.from(Activity_business.this);
        Register r = new Register("רשימת בעלי עסקים בקטמונים", Activity_business.this);
        r.showDialog(inflater);
    }

}
