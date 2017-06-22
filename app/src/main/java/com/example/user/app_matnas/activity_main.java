package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.content.Intent;
import android.widget.TextView;

/*
 * This Activity represents an screen home of app and
 * Allows navigation to all screens of app.
 */

public class activity_main extends AppCompatActivity {

    private GridView nineIcon;
    private TextView news;
    private TextView notification;
    private int[] imageIDs = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find if id's fields and init variables
        news = (TextView) findViewById(R.id.news);
        news.setText(R.string.workingHours);
        news.setSelected(true);
        nineIcon = (GridView) findViewById(R.id.activity_main);
        notification = (TextView) findViewById(R.id.badge_notification_3);

        //set Adapter main
        nineIcon.setAdapter(new AdapterMain(this, imageIDs, notification));

        nineIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                iconSelect(position);
            }
        });
    }

    //This method identifies which button is pressed and navigates to the appropriate screen
    private void iconSelect(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(activity_main.this, activity_about.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(activity_main.this, activity_news.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(activity_main.this, activity_category.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(activity_main.this, activity_project.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(activity_main.this, activity_activities.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(activity_main.this, activity_workShop.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(activity_main.this, activity_team.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(activity_main.this, activity_gallery.class);
                startActivity(intent);
                break;
            case 8:
                intent = new Intent(activity_main.this, activity_contactUs.class);
                startActivity(intent);
                break;
        }
    }

    public void onClickManager(View view) {
        Intent intent = new Intent(activity_main.this, activity_login_manager.class);
        startActivity(intent);
    }
}