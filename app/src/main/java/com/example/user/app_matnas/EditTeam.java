package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*This Activity to edit Team in app
 */


public class EditTeam extends AppCompatActivity {
    public List<Team> teamList;
    String name;
    String list[];
    String active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditTeam() {

    }

    public EditTeam(Context context, String active) {
        this.context = context;
        this.active = active;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //This method get data of DB and set in list and list set in dialog
    public void getDB() {

        teamList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.child(DB_TEAM).addListenerForSingleValueEvent
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             Team team = snapshot.getValue(Team.class);
                             teamList.add(team);
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

    private void showDialog() {

        list = new String[teamList.size()];
        if (list.length == 0) {
            Toast.makeText(context, R.string.noTeam, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for (int i = 0; i < teamList.size(); i++) {
            list[i] = teamList.get(i).getTeamName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.editTeam);

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
                name = teamList.get(selectedPosition).getTeamName();

                if (active.equals("delete")) {
                    mDatabaseRef.child(DB_TEAM).child(name).removeValue();
                    mStorageRef.child(ST_STORAGE_TEAM).child(name).delete();
                    Toast.makeText(context, R.string.successDeleteTeam, Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                    finish();
                } else if (active.equals("edit")) {
                    Team team = teamList.get(selectedPosition);
                    intent = new Intent(context, AddTeam.class);
                    intent.putExtra("edit", team);
                    mDatabaseRef.child(DB_TEAM).child(name).removeValue();
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