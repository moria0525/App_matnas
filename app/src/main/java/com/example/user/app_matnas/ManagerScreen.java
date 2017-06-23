package com.example.user.app_matnas;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/*
 * This Activity represents an management screen of app
 * Allows to manager enter each operation in screen
 */


public class ManagerScreen extends AppCompatActivity {

    private FirebaseAuth auth;
    private ListView actionsList;
    private Toolbar toolbar;
    private TextView toolBarText;
    private String[] values;
    private String edit = "edit";
    private String delete = "delete";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //find if id's fields and init variables
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText(R.string.actions_manager);
        actionsList = (ListView) findViewById(R.id.list);
        auth = FirebaseAuth.getInstance();

        // Defined Array values to show in ListView
        values = getResources().getStringArray(R.array.actions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        // Assign adapter to ListView
        actionsList.setAdapter(adapter);

        // ListView Item Click Listener
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(ManagerScreen.this, UploadImage.class);//upload images
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(ManagerScreen.this, AddNews.class);//send news
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(ManagerScreen.this, AddActivity.class);//add activity
                        startActivity(i);
                        break;
                    case 3:
                        EditActivity ed = new EditActivity(ManagerScreen.this, edit);//edit activity
                        ed.getDB();
                        break;
                    case 4:
                        ed = new EditActivity(ManagerScreen.this, delete);//delete activity
                        ed.getDB();
                        break;
                    case 5:
                        i = new Intent(ManagerScreen.this, AddProject.class); //add project
                        startActivity(i);
                        break;
                    case 6:
                        EditProject ep = new EditProject(ManagerScreen.this, edit); //edit project
                        ep.getDB();
                        break;
                    case 7:
                        ep = new EditProject(ManagerScreen.this, delete); //delete project
                        ep.getDB();
                        break;
                    case 8:
                        i = new Intent(ManagerScreen.this, AddWorkShop.class); //add WorkShop
                        startActivity(i);
                        break;
                    case 9:
                        EditWorkShop ws = new EditWorkShop(ManagerScreen.this, edit); //edit WorkShop
                        ws.getDB();
                        break;
                    case 10:
                        ws = new EditWorkShop(ManagerScreen.this, delete); //delete WorkShop
                        ws.getDB();
                        break;
                    case 11:
                        i = new Intent(ManagerScreen.this, AddTeam.class); //add team
                        startActivity(i);
                        break;
                    case 12:
                        EditTeam et = new EditTeam(ManagerScreen.this, edit); //edit team
                        et.getDB();
                        break;
                    case 13:
                        et = new EditTeam(ManagerScreen.this, delete); //delete team
                        et.getDB();
                        break;
                    case 14:
                        i = new Intent(ManagerScreen.this, AddBusiness.class); //add business
                        startActivity(i);
                        break;
                    case 15:
                        i = new Intent(ManagerScreen.this, SelectCategory.class); //edit business
                        i.putExtra("active", 1);
                        startActivity(i);
                        break;
                    case 16:
                        i = new Intent(ManagerScreen.this, SelectCategory.class); //delete business
                        i.putExtra("active", 0);
                        startActivity(i);
                        break;
                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manager, menu);
        return true;
    }

    //This method if manager click logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                auth.signOut();
                startActivity(new Intent(ManagerScreen.this, activity_main.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }
}