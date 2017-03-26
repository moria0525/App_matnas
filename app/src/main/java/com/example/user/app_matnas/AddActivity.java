package com.example.user.app_matnas;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, age, timeStart, timeEnd, description;
    private Spinner type, days;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //init screen
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("הוספת חוג חדש");

        //find id fields
        name = (EditText) findViewById(R.id.et_name_activity);
        age = (EditText) findViewById(R.id.et_age_activity);
        timeStart = (EditText) findViewById(R.id.timeStart);
        timeEnd = (EditText) findViewById(R.id.timeEnd);
        description = (EditText) findViewById(R.id.description);
        type = (Spinner) findViewById(R.id.type);
        days = (Spinner) findViewById(R.id.days);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                checkFullFields();
                break;
            case R.id.action_cancel:
                Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    private void checkFullFields()
    {
        String s_name = name.getText().toString();
        String s_age = age.getText().toString();
        String s_timeStart = timeStart.getText().toString();
        String s_timeEnd = timeEnd.getText().toString();
        String s_description = description.getText().toString();

        boolean entriesValid = true;
        try {
            if (TextUtils.isEmpty(s_name)) {
                name.setError("insert");
                entriesValid = false;
            } else if (TextUtils.isEmpty(s_age)) {
                age.setError("insert");
                entriesValid = false;
            } else if (TextUtils.isEmpty(s_timeStart)) {
                timeStart.setError("insert");
                entriesValid = false;
            } else if (TextUtils.isEmpty(s_timeEnd)) {
                timeStart.setError("insert");
                entriesValid = false;
            } else if (TextUtils.isEmpty(s_description)) {
                description.setError("insert");
                entriesValid = false;
            }

        } catch (Exception e)
        {
            Toast.makeText(context,"stop",Toast.LENGTH_LONG).show();
            entriesValid = false;
        }
        if(entriesValid)
        {
            //DatabaseReference usersRef = mDatabase.child("Nision");
            Map<String, String> userData = new HashMap<String, String>();

            userData.put("שם החוג", s_name);
            userData.put("גיל", s_age);
            userData.put("שעת התחלה", s_timeStart);
            userData.put("שעת סיום",s_timeStart);
            userData.put("תיאור",s_description);


            mDatabase.child("חוגים").setValue(userData);
            Toast.makeText(context,"finish",Toast.LENGTH_LONG).show();

            //TODO save to db
            // processbar
            //message sucess


        }

    }
}