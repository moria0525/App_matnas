package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.app_matnas.FirebaseHelper.*;

/*This Activity to edit Business in app
 */

public class EditBusiness extends AppCompatActivity {
    private List<Business> busList;
    private String name;
    private String list[];
    private Context context;
    private int active;
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

    //This method get data of DB and set in list and list set in dialog
    public void getDB(final String category, int flag) {
        active = flag;
        busList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.child(DB_BUSINESS).child(category).addListenerForSingleValueEvent
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             Business business = snapshot.getValue(Business.class);
                             busList.add(business);
                         }
                         hideProgressDialog();
                         showDialog(category);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                         hideProgressDialog();
                     }
                 }
                );
    }

    //set list business in dialog
    public void showDialog(final String category) {
        list = new String[busList.size()];
        for (int i = 0; i < busList.size(); i++) {
            list[i] = busList.get(i).getBusinessName();
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

                if (active == 0)  //delete business
                {
                    mDatabaseRef.child(DB_BUSINESS).child(category).child(name).removeValue();
                    mStorageRef.child(ST_STORAGE_BUSINESS).child(name).delete();
                    Toast.makeText(context, R.string.successDeleteBusiness, Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();

                } else if (active == 1) //edit business
                {
                    Business business = busList.get(selectedPosition);
                    intent = new Intent(context, AddBusiness.class);
                    intent.putExtra("editBusiness", business);
                    mDatabaseRef.child(DB_BUSINESS).child(category).child(name).removeValue();
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