package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {


    private ImageButton contactus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactus =(ImageButton)findViewById(R.id.contactus);

    }
    public void onClickContactUsIcon(View view)
    {
        Intent i = new Intent(this,activity_contactUs.class);
        startActivity(i);
    }

    public void onClickManagerIcon(View view)
    {
        Intent i = new Intent(this,activity_loginManager.class);
        startActivity(i);
    }

}



