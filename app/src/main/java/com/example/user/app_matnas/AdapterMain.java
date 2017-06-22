package com.example.user.app_matnas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * This Adapter of home screen
 */

public class AdapterMain extends BaseAdapter {
    public static int countNotification = 0;
    private Context context;
    private int[] imageIDs;
    public static TextView notification;

    public AdapterMain(Context c, int[] imageIDs, TextView notification) {
        context = c;
        this.imageIDs = imageIDs;
        this.notification = notification;
    }

    //---returns the number of buttons---
    public int getCount() {
        return imageIDs.length;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(7, 7, 7, 7);

        } else {
            imageView = (ImageView) convertView;
        }
        if (position == 1 && countNotification > 0) {
            notification.setVisibility(View.VISIBLE);
        }
        imageView.setImageResource(imageIDs[position]);
        return imageView;
    }
}