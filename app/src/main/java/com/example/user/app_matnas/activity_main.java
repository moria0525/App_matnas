package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.content.Intent;

public class activity_main extends AppCompatActivity {


    private GridView nineIcon;
    public static int[] imageIDs = {
            R.drawable.about,
            R.drawable.messages,
            R.drawable.event,
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

        nineIcon = (GridView) findViewById(R.id.activity_main);
        nineIcon.setAdapter(new MainAdapter(this, imageIDs));


        nineIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                iconSelect(position);
            }
        });

    }

    private void iconSelect(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(activity_main.this, activity_about.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(activity_main.this, activity_about.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(activity_main.this, activity_events.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(activity_main.this, activity_about.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(activity_main.this, activity_hobbies.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(activity_main.this, activity_about.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(activity_main.this, activity_about.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_login_manager:
                onClickManager();
                return true;
            case R.id.menu_help:
                onClickHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickManager() {
        Intent intent = new Intent(activity_main.this, activity_login_manager.class);
        startActivity(intent);
    }

    private void onClickHelp() {
        Intent intent = new Intent(activity_main.this, activity_contactUs.class);
        startActivity(intent);
    }


}

