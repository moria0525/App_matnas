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




/*This Activity to edit WorkShop in app
 */

public class EditWorkShop extends AppCompatActivity {
    public List<WorkShop> wsList;
    private String name;
    private String list[];
    private String active;
    private Context context;
    private ProgressDialog mProgressDialog;


    public EditWorkShop() {

    }


    public EditWorkShop(Context context, String active) {
        this.context = context;
        this.active = active;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //This method get data of DB and set in list and list set in dialog
    public void getDB() {

        wsList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.child(DB_WORKSHOP).addListenerForSingleValueEvent
                (new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             WorkShop workShop = snapshot.getValue(WorkShop.class);
                             wsList.add(workShop);
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

    //This method to show dialog list
    private void showDialog() {

        list = new String[wsList.size()];
        if (list.length == 0) {
            Toast.makeText(context, R.string.noWorkShop, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for (int i = 0; i < wsList.size(); i++) {
            list[i] = wsList.get(i).getWorkShopName();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.editWorkShop);

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
                name = wsList.get(selectedPosition).getWorkShopName();

                if (active.equals("delete")) {
                    mDatabaseRef.child(DB_WORKSHOP).child(name).removeValue();
                    Toast.makeText(context, R.string.successDeleteWorkShop, Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                    finish();
                } else if (active.equals("edit")) {
                    WorkShop workShop = wsList.get(selectedPosition);
                    intent = new Intent(context, AddWorkShop.class);
                    intent.putExtra("edit", workShop);
                    mDatabaseRef.child(DB_WORKSHOP).child(name).removeValue();
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
            mProgressDialog.setMessage(context.getString(R.string.loadingWorkShop));
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