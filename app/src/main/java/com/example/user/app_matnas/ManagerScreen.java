package com.example.user.app_matnas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ManagerScreen extends AppCompatActivity {

    private ListView actionsList;
    private FirebaseAuth auth;
    private EditText name, date, des;
    private ImageButton gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_screen);

        actionsList = (ListView) findViewById(R.id.List);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Defined Array values to show in ListView
        String[] values = new String[]{"העלאת תמונות לגלרייה",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        actionsList.setAdapter(adapter);

        // ListView Item Click Listener
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                switch (itemPosition) {
                    case 0:
                        uploadPictureTogallery();

                }
            }

        });
    }

    public void uploadPictureTogallery() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("העלאת תמונות לגלרייה");
        LayoutInflater inflater = LayoutInflater.from(this);

        final View layoutView = inflater.inflate(R.layout.muploadimage, null);
        builder.setView(layoutView);


        builder.setPositiveButton("אישור",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        builder.setNegativeButton("ביטול",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean entriesValid = true;
                try {
                    name = (EditText) layoutView.findViewById(R.id.et_name_event);
                    date = (EditText) layoutView.findViewById(R.id.et_date_event);
                    des = (EditText) layoutView.findViewById(R.id.et_description_event);
                    gallery = (ImageButton) layoutView.findViewById(R.id.gallery_event);

                    String s_event = name.getText().toString();
                    String s_date = date.getText().toString();
                    String s_des = des.getText().toString();
                    if (TextUtils.isEmpty(s_event))
                    {
                        name.setError("insert name");
                        entriesValid = false;

                    } else if (TextUtils.isEmpty(s_date)) {
                        date.setError("insert date");
                        entriesValid = false;
                    } else if (TextUtils.isEmpty(s_des)) {
                        des.setError("insert description");
                        entriesValid = false;
                    }
                } catch (Exception e) {
                    entriesValid = false;
                }

                if (entriesValid) {
                    dialog.dismiss();
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }

            });


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        final View layoutView = inflater.inflate(R.layout.muploadimage, null);
//        builder.setView(layoutView);
//
//        AlertDialog dialog = builder.create();
//        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        boolean entriesValid = true;
//
//        try {
//            name = (EditText) layoutView.findViewById(R.id.et_name_event);
//            date = (EditText) layoutView.findViewById(R.id.et_date_event);
//            des = (EditText) layoutView.findViewById(R.id.et_description_event);
//            gallery = (ImageButton) layoutView.findViewById(R.id.gallery_event);
//
//            String s_event = name.getText().toString();
//            String s_date = date.getText().toString();
//            String s_des = des.getText().toString();
//
//            if (TextUtils.isEmpty(s_event)) {
//                Toast.makeText(getApplicationContext(), "hi!!!!!" + s_event, Toast.LENGTH_LONG).show();
//                name.setError("הכנס שם לאירוע");
//                entriesValid = false;
//
//            } else if (TextUtils.isEmpty(s_date)) {
//                date.setError("הכנס תאריך לאירוע");
//                entriesValid = false;
//            } else if (TextUtils.isEmpty(s_des)) {
//                des.setError("הכנס תיאור לאירוע");
//                entriesValid = false;
//            }
//        } catch (Exception e) {
//            entriesValid = false;
//        }
//
//        if(!entriesValid) {
//            button.setEnabled(false);
//        }
//        else
//        button.setEnabled(true);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//
//                // get the edit text values here and pass them back via the listener
//                try {
//                    name = (EditText) layoutView.findViewById(R.id.et_name_event);
//                    date = (EditText) layoutView.findViewById(R.id.et_date_event);
//                    des = (EditText) layoutView.findViewById(R.id.et_description_event);
//                    gallery = (ImageButton) layoutView.findViewById(R.id.gallery_event);
//
//                    String s_event = name.getText().toString();
//                    String s_date = date.getText().toString();
//                    String s_des = des.getText().toString();
//
//                    if (TextUtils.isEmpty(s_event)) {
//                        Toast.makeText(getApplicationContext(), "hi!!!!!" + s_event, Toast.LENGTH_LONG).show();
//                        name.setError("הכנס שם לאירוע");
//                        entriesValid = false;
//
//                    } else if (TextUtils.isEmpty(s_date)) {
//                        date.setError("הכנס תאריך לאירוע");
//                        entriesValid = false;
//                    } else if (TextUtils.isEmpty(s_des)) {
//                        des.setError("הכנס תיאור לאירוע");
//                        entriesValid = false;
//                    }
//                } catch (Exception e) {
//                    entriesValid = false;
//                }
//                if (entriesValid)
//                    dialog.dismiss();
//            }
        // });
        //     dialog.show();
    }


//        adb.setPositiveButton("אישור", new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//
//                Dialog final_dialog = (Dialog) dialog;
//                name = (EditText)final_dialog.findViewById(R.id.et_name_event);
//                date = (EditText)final_dialog.findViewById(R.id.et_date_event);
//                des = (EditText)final_dialog.findViewById(R.id.et_description_event);
//                gallery = (ImageButton)final_dialog.findViewById(R.id.gallery_event);
//
//                String s_event = name.getText().toString();
//                String s_date =  date.getText().toString();
//                String s_des = des.getText().toString();
//
//                    if (s_event.length()==0)
//                    {
//                        Toast.makeText(getApplicationContext(),"hi!!!!!"+ s_event,Toast.LENGTH_LONG).show();
//                        name.setError("הכנס שם לאירוע");
//                        getCurrentFocus();
//                    }
//                    if (s_date.equals("")) {
//                        date.setError("הכנס תאריך לאירוע");
//                        getCurrentFocus();
//                    }
//                    if (s_des.equals("")) {
//                        des.setError("הכנס תיאור לאירוע");
//                        getCurrentFocus();
//                    }
//
//            } });
//
//
//        adb.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            } });
//        adb.show();
//
//
//    }

    public void onClickLogOut(View v) {
        auth.signOut();
        startActivity(new Intent(ManagerScreen.this, activity_main.class));
    }
}

