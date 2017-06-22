package com.example.user.app_matnas;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * This Adapter of News
 */

public class AdapterNews extends ArrayAdapter<News> {

    private android.app.Activity context;
    private int resource;
    private List<News> newsList;

    public AdapterNews(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<News> objects) {
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

    //filter news
    public void setFilter(ArrayList<News> newList) {
        newsList = new ArrayList<>();
        newsList.addAll(newList);
        notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    //This method to get time ago of news
    private String getAgo(String fire) {
        Date d1;
        Date d2;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String now = format.format(new Date());
        String result = "";

        try {
            d1 = format.parse(fire);
            d2 = format.parse(now);


            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            result = context.getString(R.string.before) + " " + diffHours + " " + context.getString(R.string.hour);
            if (diffMinutes < 1) {
                result = context.getString(R.string.before) + " " + diffSeconds + " " + context.getString(R.string.second);
            }
            if (diffHours > 24)
                result = context.getString(R.string.before) + " " + diffDays + " " + context.getString(R.string.day);
            if (diffHours == 0 && diffMinutes >= 1) {
                result = context.getString(R.string.before) + " " + diffMinutes + " " + context.getString(R.string.minute);
            }
        } catch (ParseException e1)

        {
            e1.printStackTrace();
        }
        return result;
    }

}