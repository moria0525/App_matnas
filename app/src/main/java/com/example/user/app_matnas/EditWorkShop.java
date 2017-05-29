package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.list;


public class EditWorkShop extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    public static final String DATABASE_PATH = "workShop";
    public List<WorkShop> wsList;
    String name;
    String list[];
    int active;
    Context context;
    private ProgressDialog mProgressDialog;


    public EditWorkShop() {

    }


    public EditWorkShop(Context context) {

        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void getDB(int flag) {

        wsList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        active = flag;
        showProgressDialog();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
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

    public void showDialog()
    {
        list = new String[wsList.size()];
        for (int i = 0; i < wsList.size(); i++) {
            list[i] = wsList.get(i).getWorkShopName();
        }
        if (list.length == 0) {
            backToManagerScreen();
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
                if (active == 0) {
                    mDatabaseRef.child(name).removeValue();
                    backToManagerScreen();
                    dialogInterface.dismiss();
                } else {
                    WorkShop ws = wsList.get(selectedPosition);
                    intent = new Intent(context, AddWorkShop.class);
                    intent.putExtra("editWorkShop", ws);
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
        Toast.makeText(context, "אין סדנאות", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, ManagerScreen.class);
        context.startActivity(intent);
    }

}