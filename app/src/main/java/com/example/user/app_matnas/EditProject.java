package com.example.user.app_matnas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


public class EditProject extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    public static final String DATABASE_PATH = "projects";
    public List<Project> proList;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        mDatabaseRef.addValueEventListener
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         //Fetch image data from firebase database
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             //ImageUpload class require default constructor
                             Project project = snapshot.getValue(Project.class);
                             proList.add(project);
                         }
                         showDialog();
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                 }
                );

    }

    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProject.this);
        String list[] = new String[proList.size()];
        for (int i = 0; i < proList.size(); i++)
        {
            list[i] = proList.get(i).getProjectName();
        }
        if(list.length==0)
        {
            Toast.makeText(getApplicationContext(), "אין פרוייקטים קיימים", Toast.LENGTH_SHORT).show();
            finish();
        }

        builder.setTitle(R.string.editProject)

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }

                })

                // Set the action buttons
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int active;
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        Intent i = getIntent();
                        active = i.getIntExtra("active",-1);
                        Toast.makeText(getApplicationContext(),""+active,Toast.LENGTH_LONG).show();
                        if(active == 0)
                        {
                            name = proList.get(selectedPosition).getProjectName();
                            mDatabaseRef.child(name).removeValue();
                            Toast.makeText(getApplicationContext(), "הפרוייקט נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Project project = proList.get(selectedPosition);
                            Intent intent = new Intent(EditProject.this, AddProject.class);
                            intent.putExtra("editProject", project);
                            startActivity(intent);
                        }
                    }
                })

                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        backToManagerScreen();
                    }
                })

                .show();

    }


    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        Intent i = new Intent(EditProject.this, ManagerScreen.class);
        startActivity(i);
    }
}
