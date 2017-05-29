package com.example.user.app_matnas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

public class activity_team extends AppCompatActivity
{
    private TeamAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Team> teamList = new ArrayList<>();
    private ListView list;
    private Toolbar toolbar;
    private TextView toolBarText;
    private ProgressDialog mProgressDialog;
    private View layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        toolBarText.setText("הצוות שלנו");
        list = (ListView) findViewById(R.id.list);
        getDataFromDB();
}

    public void getDataFromDB() {

        showProgressDialog();
        databaseReference.child("team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Team team = postSnapShot.getValue(Team.class);
                        teamList.add(team);
                    }
                }
                hideProgressDialog();
                adapter = new TeamAdapter(activity_team.this, R.layout.activity_team, teamList);
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
            mProgressDialog = new ProgressDialog(activity_team.this);
            mProgressDialog.setMessage("טוען את אנשי הצוות..עוד רגע..");
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
                ArrayList<Team> newList = new ArrayList<>();
                for (Team team : teamList) {
                    String name = team.getTeamName().toLowerCase();
                    String role = team.getTeamRole().toLowerCase();
                    if (name.contains(newText) || role.contains(newText)) {
                        newList.add(team);
                    }
                }
                adapter.setFilter(newList);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class TeamAdapter extends ArrayAdapter<Team> {
        private android.app.Activity context;
        private int resource;
        private List<Team> listTeam;

        public TeamAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<Team> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            listTeam = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_TName);
            TextView tvRole = (TextView) v.findViewById(R.id.tv_TRole);
            TextView tvDes = (TextView) v.findViewById(R.id.tv_TDes);
            ImageView img = (ImageView) v.findViewById(R.id.imageViewTeam);
            ImageView mail = (ImageView) v.findViewById(R.id.imageViewMail);

            mail.setImageResource(R.drawable.social_media_mail);
            tvName.setText(listTeam.get(position).getTeamName());
            tvRole.setText(listTeam.get(position).getTeamRole());
            tvDes.setText(listTeam.get(position).getTeamDes());
            Glide.with(context).load(listTeam.get(position).getTeamImage()).into(img);
            v.findViewById(R.id.imageViewMail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri mail = Uri.parse("mailto:"+listTeam.get(position).getTeamMail());
                    Intent intent = new Intent(Intent.ACTION_VIEW, mail);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "פניה אישית ל"+listTeam.get(position).getTeamName());
                    startActivity(intent);
                }
            });
            return v;

        }

        @Override
        public int getCount() {
            return listTeam.size();
        }

        public void setFilter(ArrayList<Team> newList) {
            listTeam = new ArrayList<>();
            listTeam.addAll(newList);
            notifyDataSetChanged();

        }

    }

}






