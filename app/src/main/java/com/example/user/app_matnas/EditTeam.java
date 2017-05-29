package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class EditTeam extends AppCompatActivity
{
    private DatabaseReference mDatabaseRef;
    public static final String DATABASE_PATH = "team";
    public List<Team> teamList;
    String name;

    private StorageReference mStorageRef;
    String list[];
    int active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditTeam() {

    }
    public EditTeam(Context context) {

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getDB(int flag) {

        teamList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        active = flag;
        showProgressDialog();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
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

    public void showDialog() {
        String list[] = new String[teamList.size()];
        for (int i = 0; i < teamList.size(); i++) {
            list[i] = teamList.get(i).getTeamName();
        }
        if (list.length == 0) {
            backToManagerScreen();
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
                if (active == 0) {
                    mDatabaseRef.child(name).removeValue();
                    StorageReference ref = mStorageRef.child(AddTeam.FB_STORAGE_TEAM).child(name);
                    ref.delete();
                    backToManagerScreen();
                    dialogInterface.dismiss();
                } else {
                    Team team = teamList.get(selectedPosition);
                    intent = new Intent(context, AddTeam.class);
                    intent.putExtra("editTeam", team);
                    mDatabaseRef.child(name).removeValue();
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

    /* Method to back to screen manager after saving new activity */
    private void backToManagerScreen() {
        Toast.makeText(context, "אין אנשי צוות", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, ManagerScreen.class);
        context.startActivity(intent);
    }

}
