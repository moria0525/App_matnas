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

public class EditProject extends AppCompatActivity {
    public List<Project> proList;
    String name;
    String list[];
    String active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditProject() {

    }


    public EditProject(Context context, String active)
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
        proList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.child(DB_PROJECTS).addListenerForSingleValueEvent
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             Project project = snapshot.getValue(Project.class);
                             proList.add(project);
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

        list = new String[proList.size()];
        if (list.length == 0)
        {
            Toast.makeText(context, "לא נמצאו פרויקטים",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for (int i = 0; i < proList.size(); i++) {
            list[i] = proList.get(i).getProjectName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.editProject);

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
                name = proList.get(selectedPosition).getProjectName();

                if (active.equals("delete")) {
                    mDatabaseRef.child(DB_PROJECTS).child(name).removeValue();
                    mStorageRef.child(ST_STORAGE_PROJECT).child(name).delete();
                    Toast.makeText(context, "הפרויקט נמחק בהצלחה",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    finish();
                } else if(active.equals("edit")) {
                    Project project = proList.get(selectedPosition);
                    intent = new Intent(context, AddProject.class);
                    intent.putExtra("edit", project);
                    mDatabaseRef.child(DB_PROJECTS).child(name).removeValue();
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