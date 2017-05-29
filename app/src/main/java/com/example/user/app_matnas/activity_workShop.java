package com.example.user.app_matnas;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_workShop extends AppCompatActivity {

    private WorkShopAdapter adapter;
    private DatabaseReference databaseReference;
    private List<WorkShop> wsList = new ArrayList<>();
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;
    private View layoutView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("סדנאות");
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        getDataFromDB();

    }


    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("workShop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        WorkShop workShop = postSnapShot.getValue(WorkShop.class);
                        wsList.add(workShop);
                    }
                }
                hideProgressDialog();
                adapter = new WorkShopAdapter(wsList,getApplicationContext());
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
            mProgressDialog = new ProgressDialog(activity_workShop.this);
            mProgressDialog.setMessage("טוען את הסדנאות..עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//        MenuItem item = menu.findItem(R.id.app_bar_search);
//        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
//        //searchView.setIconified(false);
//        searchView.setQueryHint("חיפוש חופשי");
//
//
//        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                newText = newText.toLowerCase();
//                ArrayList<WorkShop> newList = new ArrayList<>();
//                for (WorkShop workShop : wsList) {
//                    String name = workShop.getWorkShopName().toLowerCase();
////                    String age = activity.getActivityAge().toLowerCase();
////                    String type = activity.getActivityType().toLowerCase();
////                    String days = activity.getActivityDays().toLowerCase();
////                    String des = activity.getActivityDes().toLowerCase();
////                    String start = activity.getActivityStart().toLowerCase();
////                    String end = activity.getActivityEnd().toLowerCase();
//
//                    if (name.contains(newText))
//                    {
////                         || age.contains(newText) || type.contains(newText)
////                            || days.contains(newText) || des.contains(newText) || start.contains(newText)
////                            || end.contains(newText)
//                        newList.add(workShop);
//                    }
//                }
//                adapter.setFilter(newList);
//
//                return true;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }


    private class WorkShopAdapter extends RecyclerView.Adapter<WorkShopAdapter.UserViewHolder> {

        private List<WorkShop> wsList;
        private Context context;


        public WorkShopAdapter(List<WorkShop> wsList, Context context) {
            this.wsList = wsList;
            this.context = context;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity_workShop.this).inflate(R.layout.activity_work_shop, null);
            UserViewHolder userViewHolder = new UserViewHolder(view);
            return userViewHolder;
        }


        @Override
        public void onBindViewHolder(UserViewHolder holder, final int position) {
            final WorkShop workShop = wsList.get(position);
            holder.tvName.setText(workShop.getWorkShopName());
//            String type = activity.getActivityType().substring(0, activity.getActivityType().length() - 2);
//            holder.tvType.setText(type);
//            holder.tvAge.setText(activity.getActivityAge());
//            holder.tvDays.setText(activity.getActivityDays());
//            holder.tvStart.setText(activity.getActivityStart());
//            holder.tvEnd.setText(activity.getActivityEnd());
//            holder.tvDes.setText(activity.getActivityDes());

//            holder.button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LayoutInflater inflater = LayoutInflater.from(activity_hobbies.this);
//                    register r = new register(activity.getActivityName(), activity_hobbies.this);
//                    r.showDialog(inflater);
//                }
//            });

        }


        @Override
        public int getItemCount() {
            return wsList.size();
        }


        public class UserViewHolder extends RecyclerView.ViewHolder {

            TextView tvName;
//            TextView tvType;
//            TextView tvAge;
//            TextView tvDays;
//            TextView tvStart;
//            TextView tvEnd;
//            TextView tvDes;
//            Button button;

            public UserViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_WName);
//                tvType = (TextView) itemView.findViewById(R.id.view_type);
//                tvAge = (TextView) itemView.findViewById(R.id.view_age);
//                tvDays = (TextView) itemView.findViewById(R.id.view_days);
//                tvStart = (TextView) itemView.findViewById(R.id.view_start);
//                tvEnd = (TextView) itemView.findViewById(R.id.view_end);
//                tvDes = (TextView) itemView.findViewById(R.id.view_des);
//                button = (Button) itemView.findViewById(R.id.button3);
            }

        }


        public void setFilter(ArrayList<WorkShop> newList) {
            wsList = new ArrayList<>();
            wsList.addAll(newList);
            notifyDataSetChanged();

        }
    }
}
