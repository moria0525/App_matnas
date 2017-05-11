package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class activity_messages extends AppCompatActivity implements View.OnClickListener {

    //Declaring EditText
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;

    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        //Adding click listener
        buttonSend.setOnClickListener(this);
    }


    private void sendEmail() {
        //Getting content for email
        String email   = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        //SendMail sm = new SendMail(this, email, subject, message, s_lname, s_neighborhood, s_phone, s_email);

        //Executing sendmail to send email
       // sm.execute();
    }

    @Override
    public void onClick(View v) {
        sendEmail();
    }
}

//class Config {
//    public static final String EMAIL ="uinpat@gmail.com";
//    public static final String PASSWORD ="uinpat123";
//}