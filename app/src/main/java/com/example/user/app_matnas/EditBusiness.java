package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EditBusiness extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    public List<Business> busList;
    String name;
    String list[];
    int active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditBusiness() {

    }


    public EditBusiness(Context context) {

        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getDB(String category, int flag)
    {
        busList = new ArrayList<>();
        active = flag;
        showProgressDialog();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("business").child(category);
        mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Business business = snapshot.getValue(Business.class);
                    busList.add(business);
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

    public void showDialog()
    {
        list = new String[busList.size()];
        for (int i = 0; i < busList.size(); i++) {
            list[i] = busList.get(i).getBusinessName();
        }
        if (list.length == 0) {
            backToManagerScreen();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.editBusiness);

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
                name = busList.get(selectedPosition).getBusinessName();

                if (active == 0) {
                    mDatabaseRef.child(name).removeValue();
                    backToManagerScreen();
                    dialogInterface.dismiss();
                } else {
                    Business business = busList.get(selectedPosition);
                    intent = new Intent(context, AddBusiness.class);
                    intent.putExtra("editBusiness", business);
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
        Intent intent = new Intent(context, ManagerScreen.class);
        context.startActivity(intent);
    }
}