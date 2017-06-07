package com.example.user.app_matnas;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.app_matnas.R;
import com.example.user.app_matnas.Team;

import java.util.ArrayList;
import java.util.List;

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
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Uri mail = Uri.parse("mailto:"+listTeam.get(position).getTeamMail());
                Intent intent = new Intent(Intent.ACTION_VIEW, mail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "פניה אישית ל"+listTeam.get(position).getTeamName());
                context.startActivity(intent);
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