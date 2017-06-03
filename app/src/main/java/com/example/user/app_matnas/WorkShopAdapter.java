package com.example.user.app_matnas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WorkShopAdapter extends RecyclerView.Adapter<WorkShopAdapter.UserViewHolder> {

    private List<WorkShop> wsList;
    private Context context;


    public WorkShopAdapter(List<WorkShop> wsList, Context context) {
        this.wsList = wsList;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_work_shop, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }


    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final WorkShop workShop = wsList.get(position);
        holder.tvName.setText(workShop.getWorkShopName());
        holder.tvDate.setText(workShop.getWorkShopDate());
        holder.tvDes.setText(workShop.getWorkShopDes());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                Register r = new Register(workShop.getWorkShopName(), context);
                r.showDialog(inflater);
            }
        });

    }


    @Override
    public int getItemCount() {
        return wsList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        TextView tvDes;
        Button button;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_WName);
            tvDate = (TextView) itemView.findViewById(R.id.tv_WDate);
            tvDes = (TextView) itemView.findViewById(R.id.tv_WDes);
            button = (Button) itemView.findViewById(R.id.btn_workShop);
        }

    }

    public void setFilter(ArrayList<WorkShop> newList) {
        wsList = new ArrayList<>();
        wsList.addAll(newList);
        notifyDataSetChanged();
    }
}