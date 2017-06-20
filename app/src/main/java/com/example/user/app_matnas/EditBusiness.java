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

import static com.example.user.app_matnas.AddBusiness.FB_STORAGE_BUSINESS;
import static com.example.user.app_matnas.FirebaseHelper.*;


public class EditBusiness extends AppCompatActivity {
    public List<Business> busList;
    String name;
    String list[];
    Context context;
    int active;
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
        active = flag;
        mDatabaseRef = mDatabaseRef.child("business").child(category);
        busList = new ArrayList<>();
        showProgressDialog();
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener()
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
        for (int i = 0; i < busList.size(); i++)
        {
            list[i] = busList.get(i).getBusinessName();
        }
//        if (list.length == 0)
//        {
//            Toast.makeText(context, "לא נמצאו עסקים",Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }

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
                    mDatabaseRef.child(name).removeValue(); //////todo
                    mStorageRef.child(FB_STORAGE_BUSINESS).child(name).delete();
                    Toast.makeText(context,"בית העסק נמחק בהצלחה",Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();

                }
                else if(active == 1) {
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

}