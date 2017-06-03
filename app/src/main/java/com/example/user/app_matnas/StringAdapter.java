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

import java.util.List;


public class StringAdapter extends ArrayAdapter<String> {
    private android.app.Activity context;
    private int resource;
    private List<String> listString;

    public StringAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listString = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.ImageName);
        ImageView img = (ImageView) v.findViewById(R.id.ImageGallery);
        img.setImageResource(R.drawable.gallery_event);
        tvName.setText(listString.get(position));
        return v;

    }
}