package com.example.user.app_matnas;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * This Adapter for activities
 */


public class AdapterActivities extends RecyclerView.Adapter<AdapterActivities.UserViewHolder> {

    private List<Activity> actList;
    private Context context;


    public AdapterActivities(List<Activity> actList, Context context) {
        this.actList = actList;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_activities, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }


    //set all data in screen
    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final Activity activity = actList.get(position);
        holder.tvName.setText(activity.getActivityName());
        String type = activity.getActivityType().substring(0, activity.getActivityType().length() - 2);
        holder.tvType.setText(type);
        holder.tvAge.setText(activity.getActivityAge());
        String days = activity.getActivityDays().substring(0, activity.getActivityDays().length() - 2);
        holder.tvDays.setText(days);
        holder.tvStart.setText(activity.getActivityStart());
        holder.tvEnd.setText(activity.getActivityEnd());
        holder.tvDes.setText(activity.getActivityDes());
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);//random image
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(3, randomAndroidColor);
        drawable.setCornerRadius(8);
        drawable.setColor(randomAndroidColor);
        holder.aaa.setBackgroundDrawable(drawable);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                Register r = new Register(activity.getActivityName(), context);
                r.showDialog(inflater);
            }
        });


    }


    @Override
    public int getItemCount() {
        return actList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvType;
        TextView tvAge;
        TextView tvDays;
        TextView tvStart;
        TextView tvEnd;
        TextView tvDes;
        Button button;
        LinearLayout rv;
        LinearLayout aaa;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.view_name);
            tvType = (TextView) itemView.findViewById(R.id.view_type);
            tvAge = (TextView) itemView.findViewById(R.id.view_age);
            tvDays = (TextView) itemView.findViewById(R.id.view_days);
            tvStart = (TextView) itemView.findViewById(R.id.view_start);
            tvEnd = (TextView) itemView.findViewById(R.id.view_end);
            tvDes = (TextView) itemView.findViewById(R.id.view_des);
            button = (Button) itemView.findViewById(R.id.button3);
            rv = (LinearLayout) itemView.findViewById(R.id.rv);
            aaa = (LinearLayout) itemView.findViewById(R.id.aaa);
        }

    }

    //filter list
    public void setFilter(ArrayList<Activity> newList) {
        actList = new ArrayList<>();
        actList.addAll(newList);
        notifyDataSetChanged();

    }
}
