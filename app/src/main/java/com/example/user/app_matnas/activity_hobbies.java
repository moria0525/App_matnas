package com.example.user.app_matnas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class activity_hobbies extends AppCompatActivity {

    private HobbiesAdapter adapter;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgressDialog;
    private List<Activity> activityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView toolBarText;
    final Random rnd = new Random();
    private View layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("חוגים");
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDataFromDB();


    }

    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("activities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Activity user = postSnapShot.getValue(Activity.class);
                        activityList.add(user);
                    }
                }
                hideProgressDialog();
                adapter = new HobbiesAdapter(activityList, getApplicationContext());
                recyclerView.setAdapter(adapter);
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
            mProgressDialog.setMessage("טוען את החוגים..עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onClickRegister(View view) {

        // Toast.makeText(getApplicationContext(),""+i,Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activites, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        //searchView.setIconified(false);
        searchView.setQueryHint("חיפוש חופשי");

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Activity> newList = new ArrayList<>();
                for (Activity activity : activityList) {
                    String name = activity.getActivityName().toLowerCase();
                    String age = activity.getActivityAge().toLowerCase();
                    String type = activity.getActivityType().toLowerCase();
                    String days = activity.getActivityDays().toLowerCase();
                    String des = activity.getActivityDes().toLowerCase();
                    String start = activity.getActivityStart().toLowerCase();
                    String end = activity.getActivityEnd().toLowerCase();

                    if (name.contains(newText) || age.contains(newText) || type.contains(newText)
                            || days.contains(newText) || des.contains(newText) || start.contains(newText)
                            || end.contains(newText)) {
                        newList.add(activity);
                    }
                }
                adapter.setFilter(newList);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class HobbiesAdapter extends RecyclerView.Adapter<HobbiesAdapter.UserViewHolder> {

        private List<Activity> userList;
        private Context context;


        public HobbiesAdapter(List<Activity> userList, Context context) {
            this.userList = userList;
            this.context = context;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity_hobbies.this).inflate(R.layout.adapter_activities, null);
            UserViewHolder userViewHolder = new UserViewHolder(view);
            return userViewHolder;
        }


        @Override
        public void onBindViewHolder(UserViewHolder holder, final int position) {
            final Activity user = userList.get(position);

            //holder.ivProfilePic.setImageResource(Glide.with(context).load("gs://app-matnas-22408.appspot.com/m/1492931631390.jpg"));

            // holder.ivProfilePic.setImageURI(a);
            holder.tvName.setText(user.getActivityName());
            String type = user.getActivityType().substring(0, user.getActivityType().length() - 2);
            holder.tvType.setText(type);
            holder.tvAge.setText(user.getActivityAge());
            holder.tvDays.setText(user.getActivityDays());
            holder.tvStart.setText(user.getActivityStart());
            holder.tvEnd.setText(user.getActivityEnd());
            holder.tvDes.setText(user.getActivityDes());

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(activity_hobbies.this, register.class);
                    i.putExtra("name",user.getActivityName());
                    Toast.makeText(context,"!!!!!!!!!!!!!!!!!!!!1",Toast.LENGTH_LONG).show();
                    startActivity(i);
                }
            });

//            String[] rnd =
//                    {"https://firebasestorage.googleapis.com/v0/b/app-matnas-22408.appspot.com/o/Recycleview%2Fbutterfly.jpg?alt=media&token=42d8f6d4-463b-4cba-8dc8-89ba802b80b9",
//                            "https://firebasestorage.googleapis.com/v0/b/app-matnas-22408.appspot.com/o/Recycleview%2Fdesert.jpg?alt=media&token=bd8a4c9b-9b78-40cb-b4d2-2c23516a5b61",
//                            "https://firebasestorage.googleapis.com/v0/b/app-matnas-22408.appspot.com/o/Recycleview%2Fflower.jpg?alt=media&token=26c40c91-0cd0-4d09-b64e-0d2f19af8476" +
//                                    "https://firebasestorage.googleapis.com/v0/b/app-matnas-22408.appspot.com/o/Recycleview%2Fflowers.jpg?alt=media&token=378f8a92-737b-477e-a862-94aea46c419e",
//                            "https://firebasestorage.googleapis.com/v0/b/app-matnas-22408.appspot.com/o/Recycleview%2Fnight.jpg?alt=media&token=04eb078b-1de4-42d5-a18a-8b942f25d269"
//                    };
//
//            int idx = new Random().nextInt(rnd.length);
//            String random = (rnd[idx]);
//            Glide.with(context).load(random).into(holder.ivProfilePic);

        }


        @Override
        public int getItemCount() {
            return userList.size();
        }


        public class UserViewHolder extends RecyclerView.ViewHolder {

            ImageView ivProfilePic;
            TextView tvName;
            TextView tvType;
            TextView tvAge;
            TextView tvDays;
            TextView tvStart;
            TextView tvEnd;
            TextView tvDes;
            Button button;

            public UserViewHolder(View itemView) {
                super(itemView);
                ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
                tvName = (TextView) itemView.findViewById(R.id.view_name);
                tvType = (TextView) itemView.findViewById(R.id.view_type);
                tvAge = (TextView) itemView.findViewById(R.id.view_age);
                tvDays = (TextView) itemView.findViewById(R.id.view_days);
                tvStart = (TextView) itemView.findViewById(R.id.view_start);
                tvEnd = (TextView) itemView.findViewById(R.id.view_end);
                tvDes = (TextView) itemView.findViewById(R.id.view_des);
                button = (Button) itemView.findViewById(R.id.button3);
            }

        }


        public void setFilter(ArrayList<Activity> newList) {
            userList = new ArrayList<>();
            userList.addAll(newList);
            notifyDataSetChanged();

        }
    }

}


