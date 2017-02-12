package com.example.user.app_matnas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_login_manager extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manager);

    }

    public void onClickLogin(View view)
    {
        Intent i = new Intent(this,activity_hobbies.class);
        startActivity(i);

    }
}
