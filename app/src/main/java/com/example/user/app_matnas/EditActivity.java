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


public class EditActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    public static final String DATABASE_PATH = "activitys";
    public List<Activity> actList;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        mDatabaseRef.addValueEventListener
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         //Fetch image data from firebase database
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             //ImageUpload class require default constructor
                             Activity activity = snapshot.getValue(Activity.class);
                             actList.add(activity);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        String list[] = new String[actList.size()];
        for (int i = 0; i < actList.size(); i++)
        {
            list[i] = actList.get(i).getActivityName();
        }
        builder.setTitle(R.string.editActivity)

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
                        int delete;
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        Intent i = getIntent();
                        delete = i.getIntExtra("delete",-1);
                        if(delete == 0)
                        {
                            Toast.makeText(getApplicationContext(),"delete",Toast.LENGTH_LONG).show();
                            name = actList.get(selectedPosition).getActivityName();
                            mDatabaseRef.child(name).removeValue();
                            backToManagerScreen();
                        }
                        else {
                            Activity a = actList.get(selectedPosition);
                            Intent intent = new Intent(EditActivity.this, AddActivity.class);
                            intent.putExtra("edit", a);
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
        Intent i = new Intent(EditActivity.this, ManagerScreen.class);
        startActivity(i);
    }
}
