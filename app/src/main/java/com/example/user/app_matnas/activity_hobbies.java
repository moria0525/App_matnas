package com.example.user.app_matnas;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.internal.zzj;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class activity_hobbies extends AppCompatActivity {

    ListView allusers;
    Toolbar toolbar;
    TextView toolBarText;
    ProgressDialog mProgressDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ListingAdapter adapter;
    ArrayList<Activity> users = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        //  toolBarText.setText("חוגים");

        allusers = (ListView) findViewById(R.id.allusers);
        adapter = new ListingAdapter(this, users);
        allusers.setAdapter(adapter);
        getDataFromServer();
    }


    // getting the data from UserNode at Firebase and then adding the users in Arraylist and setting it to Listview
    public void getDataFromServer() {
        showProgressDialog();
        databaseReference.child("activitys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Activity user = postSnapShot.getValue(Activity.class);
                        users.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_hobbies.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private class ListingAdapter extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        ArrayList<Activity> users;

        public ListingAdapter(Context con, ArrayList<Activity> users) {
            context = con;
            layoutInflater = LayoutInflater.from(context);
            this.users = users;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.adapter_listing, null, false);
                holder = new ViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.view_name);
                holder.type = (TextView) convertView.findViewById(R.id.view_type);
                holder.age = (TextView) convertView.findViewById(R.id.view_age);
                holder.days = (TextView) convertView.findViewById(R.id.view_days);
                holder.start = (TextView) convertView.findViewById(R.id.view_start);
                holder.end = (TextView) convertView.findViewById(R.id.view_end);
                holder.des = (TextView) convertView.findViewById(R.id.view_des);


                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Activity user = users.get(position);

            holder.name.setText(user.getActivityName());
            holder.type.setText(user.getActivityType());
            holder.age.setText(user.getActivityAge());
            holder.days.setText(user.getActivityDays());
            holder.start.setText(user.getActivityStart());
            holder.end.setText(user.getActivityEnd());
            holder.des.setText(user.getActivityDes());


            return convertView;
        }


        public class ViewHolder {
            TextView name, type, age, days, start, end, des;

        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activites, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}


