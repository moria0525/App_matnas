package com.example.user.app_matnas;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

//public class ManagerAdapter extends ListAdapter
//{
//    private int resource;
//    private LayoutInflater inflater;
//    private Context context;
//
//    public ManagerAdapter(Context context, int resourceId, List<DataSource> objects) {
//        super(context, resourceId, objects);
//        resource = resourceId;
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//    }
//
//    private static class ViewHolder {
//        TextView nameTextView;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//
//        //create a ViewHolder reference
//        ViewHolder holder;
//
//        //check to see if the reused view is null or not, if is not null then reuse it
//        if (convertView == null) {
//            convertView = (LinearLayout) inflater.inflate(resource, null);
//            DataSource dataSource = getItem(position);
//
//            holder = new ViewHolder();
//            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
//            holder.nameTextView.setText(dataSource.getNameText());
//
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        return convertView;
//    }
//
//}