package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class Activity_main extends AppCompatActivity {

    private GridView nineIcon;
    public int[] imageIDs = {
            R.drawable.about,
            R.drawable.news,
            R.drawable.business,
            R.drawable.projects,
            R.drawable.hobbies,
            R.drawable.workshops,
            R.drawable.team,
            R.drawable.gallery,
            R.drawable.contactus
    };

    private FirebaseAuth auth;
    TextView news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        news = (TextView)findViewById(R.id.news);
        news.setText("שעות פעילות המרכז:ראשון עד חמישי בין השעות 8:30-19:00");
        news.setSelected(true);


        nineIcon = (GridView) findViewById(R.id.activity_main);
        TextView notification =  (TextView)findViewById(R.id.badge_notification_3);

        nineIcon.setAdapter(new MainAdapter(this, imageIDs,notification));

        nineIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                iconSelect(position);
            }
        });

        auth = FirebaseAuth.getInstance();


    }


    private void iconSelect(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(Activity_main.this, Activity_about.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(Activity_main.this, Activity_news.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(Activity_main.this, Activity_business.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(Activity_main.this,Activity_project.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(Activity_main.this, Activity_activities.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(Activity_main.this, Activity_workShop.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(Activity_main.this, Activity_team.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(Activity_main.this, Activity_gallery.class);
                startActivity(intent);
                break;
            case 8:
                intent = new Intent(Activity_main.this, Activity_contactUs.class);
                startActivity(intent);
                break;

        }

    }

    public void onClickManager(View view) {
        Intent intent = new Intent(Activity_main.this, Activity_login_manager.class);
        startActivity(intent);
    }


}

