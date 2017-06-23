package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*This Activity to edit Business in app
 */

public class EditActivity extends AppCompatActivity {
    public List<Activity> actList;
    String name;
    String list[];
    String active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditActivity() {

    }


    public EditActivity(Context context, String active) {
        this.context = context;
        this.active = active;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //This method get data of DB and set in list and list set in dialog
    public void getDB() {
        actList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.child(DB_ACTIVITIES).addListenerForSingleValueEvent
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             Activity activity = snapshot.getValue(Activity.class);
                             actList.add(activity);
                         }
                         hideProgressDialog();
                         showDialog();
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                         hideProgressDialog();
                     }
                 }
                );
    }

    //show list in dialog
    private void showDialog() {

        list = new String[actList.size()];
        if (list.length == 0) {
            Toast.makeText(context, R.string.noActivity, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for (int i = 0; i < actList.size(); i++) {
            list[i] = actList.get(i).getActivityName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.editActivity);

        builder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent;
                int selectedPosition = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();
                name = actList.get(selectedPosition).getActivityName();

                if (active.equals("delete")) {
                    mDatabaseRef.child(DB_ACTIVITIES).child(name).removeValue();
                    Toast.makeText(context, R.string.successDeleteActivity, Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                    finish();
                } else if (active.equals("edit")) {
                    Activity activity = actList.get(selectedPosition);
                    intent = new Intent(context, AddActivity.class);
                    intent.putExtra("edit", activity);
                    mDatabaseRef.child(DB_ACTIVITIES).child(name).removeValue();
                    context.startActivity(intent);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //This method show progress dialog until loading all data
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    //This method dismiss progress dialog if show after loading all data
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}