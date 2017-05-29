package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, age, timeStart, timeEnd, description;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("activities");

    private String s_type, s_days;
    private String error;
    private String[] AlertDialogItemsType;
    private String[] AlertDialogItemsDays;
    private List<String> ItemsIntoList;

    private boolean[] SelectedtruefalseType = new boolean[3];
    private boolean[] SelectedtruefalseDays = new boolean[5];

    private AlertDialog.Builder alertdialogbuilder;

    private Activity a_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        //init screen and variables
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[2]);
        AlertDialogItemsType = getResources().getStringArray(R.array.itemsType);
        AlertDialogItemsDays = getResources().getStringArray(R.array.itemsDays);
        Arrays.fill(SelectedtruefalseType, false);
        Arrays.fill(SelectedtruefalseDays, false);
        s_type = "";
        s_days = "";

        //find id fields
        name = (EditText) findViewById(R.id.et_name_activity);
        age = (EditText) findViewById(R.id.et_age_activity);
        timeStart = (EditText) findViewById(R.id.timeStart);
        timeEnd = (EditText) findViewById(R.id.timeEnd);
        description = (EditText) findViewById(R.id.description);


        a_edit = (Activity) getIntent().getSerializableExtra("edit");

        if (a_edit != null)
        {
            toolBarText.setText(getResources().getStringArray(R.array.actions)[3]);
            fillFields();
        }
    }

    private void fillFields() {
        name.setText(a_edit.getActivityName());
        age.setText(a_edit.getActivityAge());
        timeStart.setText(a_edit.getActivityStart());
        timeEnd.setText(a_edit.getActivityEnd());
        description.setText(a_edit.getActivityDes());
    }

    /*Method to select types of activity*/
    public void onClickType(View v) {
        s_type = "";
        alertdialogbuilder = new AlertDialog.Builder(AddActivity.this);

        ItemsIntoList = Arrays.asList(AlertDialogItemsType);

        alertdialogbuilder.setMultiChoiceItems(AlertDialogItemsType, SelectedtruefalseType, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });

        alertdialogbuilder.setCancelable(false);

        alertdialogbuilder.setTitle(getResources().getString(R.string.selectType));

        alertdialogbuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int a = 0;
                while (a < SelectedtruefalseType.length) {
                    boolean value = SelectedtruefalseType[a];
                    if (value) {
                        s_type += ItemsIntoList.get(a) + ", ";
                    }
                    a++;
                }
            }
        });

        alertdialogbuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = alertdialogbuilder.create();
        dialog.show();
    }

    /*Method to select days of activity*/
    public void onClickDays(View v) {

        alertdialogbuilder = new AlertDialog.Builder(AddActivity.this);

        ItemsIntoList = Arrays.asList(AlertDialogItemsDays);

        alertdialogbuilder.setMultiChoiceItems(AlertDialogItemsDays, SelectedtruefalseDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });

        alertdialogbuilder.setCancelable(false);

        alertdialogbuilder.setTitle(getResources().getString(R.string.selectDays));

        alertdialogbuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int a = 0;
                while (a < SelectedtruefalseDays.length) {
                    boolean value = SelectedtruefalseDays[a];
                    if (value) {
                        s_days += ItemsIntoList.get(a) + " ";
                    }
                    a++;
                }
            }
        });

        alertdialogbuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = alertdialogbuilder.create();
        dialog.show();
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
                checkFullFields();
                break;
            case R.id.action_cancel:
                openMessage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    /*Method to see message if click cancel without saving*/
    public void openMessage() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancel));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
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

    /*Method to selset time of activity*/
    public void ClickOnTime(final View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (view.getId() == R.id.timeStart) {
                    timeStart.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                } else if (view.getId() == R.id.timeEnd) {
                    timeEnd.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(getResources().getString(R.string.selectTime));
        mTimePicker.show();
    }

    /*Method to validation fields in form */
    private void checkFullFields() {
        error = "";
        String s_name = name.getText().toString();
        String s_age = age.getText().toString();
        String s_timeStart = timeStart.getText().toString();
        String s_timeEnd = timeEnd.getText().toString();
        String s_description = description.getText().toString();

        boolean entriesValid = true;
        try {
            if (TextUtils.isEmpty(s_name)) {
                error += getResources().getStringArray(R.array.error)[0] + "\n";
                entriesValid = false;
            }
            if (!checkArray(SelectedtruefalseType)) {
                error += getResources().getStringArray(R.array.error)[1] + "\n";
                entriesValid = false;
            }
            if (TextUtils.isEmpty(s_age)) {
                error += getResources().getStringArray(R.array.error)[2] + "\n";
                entriesValid = false;
            }
            if (!checkArray(SelectedtruefalseDays)) {
                error += getResources().getStringArray(R.array.error)[3] + "\n";
                entriesValid = false;
            }
            if (TextUtils.isEmpty(s_timeStart)) {
                error += getResources().getStringArray(R.array.error)[4] + "\n";
                entriesValid = false;
            }
            if (TextUtils.isEmpty(s_timeEnd)) {
                error += getResources().getStringArray(R.array.error)[5] + "\n";
                entriesValid = false;
            }
            if (TextUtils.isEmpty(s_description)) {
                error += getResources().getStringArray(R.array.error)[6] + "\n";
                entriesValid = false;
            }

        } catch (Exception e) {
            entriesValid = false;
        }
        if (!entriesValid) {
            errorFields();
        } else {
            //add data to DB
            Activity activity = new Activity(s_name, s_type, s_description, s_age, s_days, s_timeStart, s_timeEnd);
            try {
                mDatabase.child(s_name).setValue(activity);
                //activity.setActivityBType(SelectedtruefalseType);
                Toast.makeText(getApplicationContext(), "החוג נוסף בהצלחה", Toast.LENGTH_LONG).show();
                backToManagerScreen();
            } catch (DatabaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }

    /* Method to build dialog error*/
    private void errorFields() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle(getResources().getString(R.string.errorDialog));
        builder.setMessage(error);
        builder.setPositiveButton(getResources().getString(R.string.understand), null);
        builder.show();
    }

    /* Method to check if select items in multiple choice items */
    private boolean checkArray(boolean[] temp) {
        boolean flag = false;
        for (boolean b : temp)
            if (b) {
                flag = true;
                break;
            }
        return flag;
    }

    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen()
    {
        Toast.makeText(getApplicationContext(),"טופל",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddActivity.this, ManagerScreen.class);
        startActivity(intent);
        finish();
    }
}