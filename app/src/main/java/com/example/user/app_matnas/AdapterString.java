package com.example.user.app_matnas;

import android.app.*;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;

import java.util.List;



public class AdapterString extends ArrayAdapter<String> {
    private android.app.Activity context;
    private int resource;
    private List<String> listString;
    public AdapterString(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context,resource, objects);
        this.context = context;
        this.resource = resource;
        listString = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource, null);
     //   Toast.makeText(context, "position) : "+ position,Toast.LENGTH_LONG).show();
      //  Toast.makeText(context, "listString.get(position) : "+ listString.get(position),Toast.LENGTH_LONG).show();
        TextView tvName = (TextView) v.findViewById(R.id.ImageName);
        //ImageView img = (ImageView) v.findViewById(R.id.ImageGallery);
        tvName.setText(listString.get(position));
        return v;

    }


}

