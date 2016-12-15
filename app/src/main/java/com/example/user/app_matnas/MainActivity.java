package com.example.user.app_matnas;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Toast;

import java.util.Locale;

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

}



