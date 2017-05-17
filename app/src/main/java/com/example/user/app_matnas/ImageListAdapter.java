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



public class ImageListAdapter extends ArrayAdapter<String> {
    private android.app.Activity context;
    private int resource;
    private List<String> listUrl;
    public ImageListAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context,resource, objects);
        this.context = context;
        this.resource = resource;
        listUrl = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource, null);
        //TextView tvName = (TextView) v.findViewById(R.id.ImageName);
        //tvName.setText(listString.get(position));

        ImageView img = (ImageView) v.findViewById(R.id.imgView);

       img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(listUrl.get(position)).into(img);
        return v;

    }


}



