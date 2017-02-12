package com.example.user.app_matnas;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class activity_hobbies extends ListActivity {

    ArrayList<DataSource> data = new ArrayList<DataSource>();
    String[] nameTexts = {
            "שליחת הודעה למשתמשים", "עדכון מידע", "העלאת תמונות לגלריה", "הוספת אירוע",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hobbies);

        setListAdapter(new HobbiesAdapter(activity_hobbies.this, R.layout.list_item, this.getAllAnimal()));

    }

    public ArrayList<DataSource> getAllAnimal() {
        for (int i = 0; i < nameTexts.length; i++) {
            data.add(new DataSource(nameTexts[i]));
        }

        return data;
    }
}

//
//
//    private Spinner type;
//    private Spinner age;
//    private ArrayAdapter<String> dataAdapterTypes;
//    private ArrayAdapter<String> dataAdapterAges;
//    Spinner general_spinner;
//    String typeSelect, ageSelect;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hobbies);
//    }
//
////
////        // Spinner element implements OnItemSelectedListener
////        type = (Spinner) findViewById(R.id.type);
////        age = (Spinner) findViewById(R.id.age);
////
////
////        String[] types = {"בחר","ספורט","ריקוד"};
////
////        String[] ages = getResources().getStringArray(R.array.ages);
////
////        // Spinner click listener
////        type.setOnItemSelectedListener(this);
////        age.setOnItemSelectedListener(this);
////
////
////        // Creating adapter for spinner
////        dataAdapterTypes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
////        dataAdapterAges = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ages);
//        // Drop down layout style - list view with radio button
//        dataAdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterAges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        type.setPrompt("aaaa");
//        // attaching data adapter to spinner
//        type.setAdapter(dataAdapterTypes);
//        age.setAdapter(dataAdapterAges);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//        general_spinner = (Spinner) parent;
//        if (general_spinner.getId() == R.id.type) {
//            typeSelect = parent.getItemAtPosition(position).toString();
//        }
//        if (general_spinner.getId() == R.id.age) {
//            ageSelect = parent.getItemAtPosition(position).toString();
//        }
//    }
//
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//    }
//
//    public void onClickFilter(View view) {
//        Toast.makeText(getApplicationContext(), "" + typeSelect, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "" + ageSelect, Toast.LENGTH_LONG).show();
//        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
//        // myAlert.setMessage("החוג מתקיים בימי רביעי בשעה 17:00")
//        myAlert.setMessage(activity_hobbies.this.getString(R.string.hobbies))
//                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setTitle("חוג " + typeSelect + " - " + ageSelect)
//                .create();
//        myAlert.show();
//    }


