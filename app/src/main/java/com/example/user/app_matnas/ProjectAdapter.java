package com.example.user.app_matnas;

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

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends ArrayAdapter<Project> {

    private android.app.Activity context;
        private int resource;
        private List<Project> listProjects;

        public ProjectAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<Project> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            listProjects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_PName);
            ImageView img = (ImageView) v.findViewById(R.id.imageViewProject);
            ImageView arrow = (ImageView) v.findViewById(R.id.arrow);
            arrow.setImageResource(R.drawable.next);
            tvName.setText(listProjects.get(position).getProjectName());
            Glide.with(context).load(listProjects.get(position).getProjectLogo()).into(img);
            return v;

        }

        @Override
        public int getCount() {
            return listProjects.size();
        }

        public void setFilter(ArrayList<Project> newList) {
            listProjects = new ArrayList<>();
            listProjects.addAll(newList);
            notifyDataSetChanged();

        }

    }