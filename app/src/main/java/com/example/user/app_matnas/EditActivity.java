package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

public class EditActivity extends AppCompatActivity {
    public List<String> actList;

    String name;
    private ListView listView;
    private Toolbar toolbar;
    private TextView toolBarText;
    Context context;
    private ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        flag = getIntent().getStringExtra("action");

        context = getApplicationContext();
        actList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("בחר חוג");

        showProgressDialog();
        getDB();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Activity activity;

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                final String select = (String) parent.getItemAtPosition(position);
                final DatabaseReference child = mDatabaseRef.child(DB_ACTIVITIES).child(select);
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot)
                    {
                        activity = snapshot.getValue(Activity.class);
                        if (flag.equals("edit"))
                        {
                            finish();
                            Intent i = new Intent(EditActivity.this, AddActivity.class);
                            Toast.makeText(context,"here",Toast.LENGTH_SHORT).show();
                            i.putExtra("edit", activity);
                            startActivity(i);
                            finish();
                        }
                        else if (flag.equals("delete"))
                        {
                            finish();
                            child.removeValue();
                            Toast.makeText(context, "החוג נמחק בהצלחה", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

            }
        });


    }

    public void getDB() {

        mDatabaseRef.child(DB_ACTIVITIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String activity = snapshot.getValue(Activity.class).getActivityName();
                    actList.add(activity);
                }
                hideProgressDialog();
                if (actList.size() == 0) {
                    Toast.makeText(context, "לא נמצאו חוגים", Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, actList);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
                                                                }
        );
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(EditActivity.this);
            mProgressDialog.setMessage("עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}