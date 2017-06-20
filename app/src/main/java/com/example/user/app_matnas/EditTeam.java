package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
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

public class EditTeam extends AppCompatActivity {
    public List<Team> teamList;
    String name;
    String list[];
    String active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditTeam() {

    }


    public EditTeam(Context context, String active)
    {
        this.context = context;
        this.active = active;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getDB()
    {

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

    private void showDialog()
    {

        list = new String[teamList.size()];
        if (list.length == 0)
        {
            Toast.makeText(context, "לא נמצאו אנשי צוות",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "איש הצוות נמחק בהצלחה",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    finish();
                } else if(active.equals("edit")) {
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}