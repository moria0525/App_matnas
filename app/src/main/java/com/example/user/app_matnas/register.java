package com.example.user.app_matnas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
    String s_fname, s_lname, s_neighborhood, s_phone, s_email;
    String title, name;

    AlertDialog alertDialog;


    public register(String name, Context context)
    {

        this.name = name;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showDialog(final LayoutInflater inflater) {



        title = "הרשמה ל" + name;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = inflater.inflate(R.layout.activity_register, null);
        builder.setView(dialogView);
        builder.setTitle(title);

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
        alertDialog = builder.create();
        alertDialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_fname = (EditText) dialogView.findViewById(R.id.register_fname);
                register_lname = (EditText) dialogView.findViewById(R.id.register_lname);
                register_neighborhood = (EditText) dialogView.findViewById(R.id.register_neighborhood);
                register_phone = (EditText) dialogView.findViewById(R.id.register_phone);
                register_email = (EditText) dialogView.findViewById(R.id.register_email);

                inputLayoutfName = (TextInputLayout) dialogView.findViewById(R.id.input_layout_fname);
                inputLayoutlName = (TextInputLayout) dialogView.findViewById(R.id.input_layout_lname);
                inputLayoutNeighborhood = (TextInputLayout) dialogView.findViewById(R.id.input_layout_neighborhood);
                inputLayoutPhone = (TextInputLayout) dialogView.findViewById(R.id.input_layout_phone);
                inputLayoutEmail = (TextInputLayout) dialogView.findViewById(R.id.input_layout_email);

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

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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

    private void SendData() {
        //Creating SendMail object
        SendMail sm = new SendMail(context, title, s_fname, s_lname, s_neighborhood, s_phone, s_email);

        //Executing sendmail to send email
        sm.execute();
    }

    //todo get string fatal??????
    private boolean validatefName() {
        if (s_fname.isEmpty()) {
            inputLayoutfName.setError(context.getString(R.string.err_msg_fname));
            requestFocus(register_fname);
            return false;
        } else {
            inputLayoutfName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatelName() {
        if (s_lname.isEmpty()) {
            inputLayoutlName.setError(context.getString(R.string.err_msg_lname));
            requestFocus(register_lname);
            return false;
        } else {
            inputLayoutlName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNeighborhood() {
        if (s_neighborhood.isEmpty()) {
            inputLayoutNeighborhood.setError(context.getString(R.string.err_msg_neighborhood));
            requestFocus(register_neighborhood);
            return false;
        } else {
            inputLayoutNeighborhood.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {

        if (s_email.isEmpty() || !isValidEmail(s_email)) {
            inputLayoutEmail.setError(context.getString(R.string.err_msg_email));
            requestFocus(register_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (s_phone.isEmpty() || s_phone.length() != 10) {
            inputLayoutPhone.setError(context.getString(R.string.err_msg_phone));
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
            WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
            lp.dimAmount = 0.0F;
            alertDialog.getWindow().setAttributes(lp);
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
           // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        alertDialog.dismiss();
//        if()
//        Intent i = new Intent(context, activity_main.class);
//        context.startActivity(i);
//        finish();
    }

}

class Config {
    public static final String EMAIL = "uinpat@gmail.com";
    public static final String PASSWORD = "uinpat123";
}