package com.example.user.app_matnas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {


    private EditText register_fname, register_lname, register_neighborhood, register_phone, register_email;
    private TextInputLayout inputLayoutfName, inputLayoutlName, inputLayoutNeighborhood, inputLayoutEmail, inputLayoutPhone;
    private Context context;
    private View layoutView;
    String s_fname, s_lname, s_neighborhood, s_phone, s_email;
    String title,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();


        Toast.makeText(context,"after layout",Toast.LENGTH_LONG).show();
        showDialog();
    }

    private void showDialog()
    {
        final AlertDialog.Builder builder;
        final AlertDialog dialog;
        Toast.makeText(context,"showDialog",Toast.LENGTH_LONG).show();
        builder = new AlertDialog.Builder(this);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        Toast.makeText(context,name,Toast.LENGTH_LONG).show();
        title = "הרשמה לחוג " + name;
        builder.setTitle(title);
        LayoutInflater inflater = LayoutInflater.from(this);

        layoutView = inflater.inflate(R.layout.activity_register, null);

        builder.setView(layoutView);


        builder.setPositiveButton("אישור",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton("ביטול",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    boolean entriesValid = true;
                    register_fname = (EditText) layoutView.findViewById(R.id.register_fname);
                    register_lname = (EditText) layoutView.findViewById(R.id.register_lname);
                    register_neighborhood = (EditText) layoutView.findViewById(R.id.register_neighborhood);
                    register_phone = (EditText) layoutView.findViewById(R.id.register_phone);
                    register_email = (EditText) layoutView.findViewById(R.id.register_email);

                    inputLayoutfName = (TextInputLayout)layoutView.findViewById(R.id.input_layout_fname);
                    inputLayoutlName = (TextInputLayout) layoutView.findViewById(R.id.input_layout_lname);
                    inputLayoutNeighborhood = (TextInputLayout) layoutView.findViewById(R.id.input_layout_neighborhood);
                    inputLayoutPhone = (TextInputLayout) layoutView.findViewById(R.id.input_layout_phone);
                    inputLayoutEmail = (TextInputLayout) layoutView.findViewById(R.id.input_layout_email);

                    register_fname.addTextChangedListener(new MyTextWatcher(register_fname));
                    register_lname.addTextChangedListener(new MyTextWatcher(register_lname));
                    register_neighborhood.addTextChangedListener(new MyTextWatcher(register_neighborhood));
                    register_phone.addTextChangedListener(new MyTextWatcher(register_phone));
                    register_email.addTextChangedListener(new MyTextWatcher(register_email));

                    s_fname = register_fname.getText().toString().trim();
                    s_lname = register_lname.getText().toString().trim();
                    s_neighborhood = register_neighborhood.getText().toString().trim();
                    s_phone = register_phone.getText().toString().trim();
                    s_email = register_email.getText().toString().trim();


                    submitForm();
           }
         }
        );

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                backToScreen();
            }

        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validatefName()) {
            return;
        }
        if (!validatelName()) {
            return;
        }
        if (!validateNeighborhood()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        SendData();
        backToScreen();
    }

    private void SendData()
    {
        //Creating SendMail object
        SendMail sm = new SendMail(this, title,s_fname,s_lname,s_neighborhood, s_phone,s_email);

        //Executing sendmail to send email
        sm.execute();
    }

    private boolean validatefName()
    {
        if (s_fname.isEmpty()) {
            inputLayoutfName.setError(getString(R.string.err_msg_fname));
            requestFocus(register_fname);
            return false;
        } else {
            inputLayoutfName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatelName() {
        if (s_lname.isEmpty()) {
            inputLayoutlName.setError(getString(R.string.err_msg_lname));
            requestFocus(register_lname);
            return false;
        } else {
           inputLayoutlName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNeighborhood() {
        if (s_neighborhood.isEmpty()) {
            inputLayoutNeighborhood.setError(getString(R.string.err_msg_neighborhood));
            requestFocus(register_neighborhood);
            return false;
        } else {
            inputLayoutNeighborhood.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {

        if (s_email.isEmpty() || !isValidEmail(s_email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(register_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (s_phone.isEmpty() || s_phone.length() != 10) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(register_phone);
            return false;
        } else {
           inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.register_fname:
                    validatefName();
                    break;
                case R.id.register_lname:
                    validatelName();
                    break;
                case R.id.register_neighborhood:
                    validateNeighborhood();
                    break;
                case R.id.register_email:
                    validateEmail();
                    break;
                case R.id.register_phone:
                    validatePhone();
                    break;
            }
        }
    }
    /* Method to back to screen manager after saving new activity */
    private void backToScreen() {
        Intent i = new Intent(register.this, activity_hobbies.class);
        startActivity(i);
    }

}
class Config {
    public static final String EMAIL ="uinpat@gmail.com";
    public static final String PASSWORD ="uinpat123";
}
