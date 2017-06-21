package com.example.user.app_matnas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseException;


import java.util.Calendar;

import static com.example.user.app_matnas.FirebaseHelper.*;


public class AddWorkShop extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolBarText;
    private EditText name, date, description;
    private WorkShop w_edit;
    private WorkShop workShop;
    private String old = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_shop);

        //init screen and variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(getResources().getStringArray(R.array.actions)[8]);

        //find id fields
        name = (EditText) findViewById(R.id.et_name_workshop);
        date = (EditText) findViewById(R.id.et_date_workshop);
        description = (EditText) findViewById(R.id.et_des_workshop);


        w_edit = (WorkShop) getIntent().getSerializableExtra("edit");

        if (w_edit != null) {
            old = w_edit.getWorkShopName();
            toolBarText.setText(getResources().getStringArray(R.array.actions)[9]);
            fillFields();
        }


    }

    private void fillFields() {
        name.setText(w_edit.getWorkShopName());
        date.setText(w_edit.getWorkShopDate());
        description.setText(w_edit.getWorkShopDes());
    }

    public void onClickDate(View v) {

        //To show current date in the datepicker
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(AddWorkShop.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                date.setText(selectedday + "/" + ++selectedmonth + "/" + selectedyear);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("בחר תאריך");
        mDatePicker.show();
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
                saveDB();
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
        alertDialogBuilder.setMessage(getResources().getString(R.string.clickCancelWorkShop));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        finish();
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


    /*Method to validation fields in form */
    private void saveDB() {
        boolean flag = true;
        final String s_name = name.getText().toString();
        final String s_date = date.getText().toString();
        final String s_description = description.getText().toString();

        if (TextUtils.isEmpty(s_name)) {
            name.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_date)) {
            date.setError("");
            flag = false;
        }
        if (TextUtils.isEmpty(s_description)) {
            description.setError("");
            flag = false;
        }

        //add data to DB
        if (flag) {
            workShop = new WorkShop(s_name, s_date, s_description);
            try {
                mDatabaseRef.child(DB_WORKSHOP).child(s_name).setValue(workShop);
                if (!old.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "הסנדא התעדכנה בהצלחה", Toast.LENGTH_LONG).show();
                    finish();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "הסדנא נוספה בהצלחה", Toast.LENGTH_LONG).show();
                    finish();
                }

            } catch (DatabaseException e) {
                Toast.makeText(getApplicationContext(),"שגיאה: "+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


}
