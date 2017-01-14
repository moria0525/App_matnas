package com.example.user.app_matnas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class activity_hobbies extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner type;
    private Spinner age;
    private List<String> types;
    private List<String> ages;
    private ArrayAdapter<String> dataAdapterTypes;
    private ArrayAdapter<String> dataAdapterAges;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);

        // Spinner element
        type = (Spinner) findViewById(R.id.type);
        age  = (Spinner) findViewById(R.id.age);
        // Spinner click listener
        type.setOnItemSelectedListener(this);
        age.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        types = new ArrayList<String>();
        types.add("");
        types.add("אמנות");
        types.add("ספורט");
        types.add("מחשבים");
        types.add("השכלה");

        ages = new ArrayList<String>();
        ages.add("");
        ages.add("הגיל הרך");
        ages.add("ילדים");
        ages.add("מבוגרים");
        ages.add("הגיל השלישי");

        // Creating adapter for spinner
        dataAdapterTypes  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,types);
        dataAdapterAges = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ages);
        // Drop down layout style - list view with radio button
        dataAdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterAges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        type.setAdapter(dataAdapterTypes);
        age.setAdapter(dataAdapterAges);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}