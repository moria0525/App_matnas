package com.example.user.app_matnas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;

import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.user.app_matnas.MainAdapter.countNotification;
import static com.example.user.app_matnas.MainAdapter.notification;


public class activity_messages extends AppCompatActivity {
    private NewsAdapter adapter;
    private DatabaseReference databaseReference;
    private List<News> newsList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        countNotification = 0;
        notification.setText("" + countNotification);
        if(countNotification <= 0) {
            notification.setVisibility(View.INVISIBLE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("מה חדש במרכז");
        list = (ListView) findViewById(R.id.list);

        getDataFromDB();

    }


    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        News news = postSnapShot.getValue(News.class);
                        newsList.add(news);
                    }
                }
                Collections.reverse(newsList);
                hideProgressDialog();
                adapter = new NewsAdapter(activity_messages.this, R.layout.activity_messages, newsList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity_messages.this);
            mProgressDialog.setMessage("טוען את החדשות..עוד רגע..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("חיפוש חופשי");


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<News> newList = new ArrayList<>();
                for (News news : newsList) {
                    String name = news.getNewsContent().toLowerCase();
                    String date = news.getNewsDate().toLowerCase();
                    if (name.contains(newText) || date.contains(newText)) {
                        newList.add(news);
                    }
                }
                adapter.setFilter(newList);

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    public class NewsAdapter extends ArrayAdapter<News> {

        private android.app.Activity context;
        private int resource;
        private List<News> newsList;

        public NewsAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<News> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            newsList = objects;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            final View v = inflater.inflate(resource, null);
            TextView tvName = (TextView) v.findViewById(R.id.textView2);
            TextView tvDate = (TextView) v.findViewById(R.id.textView3);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            String date = getAgo(newsList.get(position).getNewsDate());

            tvName.setText(newsList.get(position).getNewsContent());
            tvDate.setText(date);

            if (!newsList.get(position).getNewsImage().isEmpty()) {
                Glide.with(context).load(newsList.get(position).getNewsImage()).into(img);
            } else {
               img.setVisibility(View.INVISIBLE);
            }
            return v;

        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        public void setFilter(ArrayList<News> newList) {
            newsList = new ArrayList<>();
            newsList.addAll(newList);
            notifyDataSetChanged();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)

        private String getAgo(String fire) {
            Date d1;
            Date d2;

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String now = format.format(new Date());
            String result = "";

            try {
                d1 = format.parse(fire);
                d2 = format.parse(now);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                result = "לפני " + diffHours + " שעות";
                if(diffMinutes < 1)
                {
                    result = "לפני " + diffSeconds + " שניות";
                }
                if(diffHours > 24)
                    result = "לפני " + diffDays + "ימים";
                if(diffHours == 0 && diffMinutes >=1)
                {
                    result = "לפני " + diffMinutes + " דקות";
                }


            } catch (ParseException e1)

            {
                e1.printStackTrace();
            }
            return result;
        }
    }

