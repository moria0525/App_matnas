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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.app_matnas.R;
import com.example.user.app_matnas.Team;

import java.util.ArrayList;
import java.util.List;

public class EsekAdapter extends ArrayAdapter<Business> {
    private android.app.Activity context;
    private int resource;
    private List<Business> listBus;

    public EsekAdapter(@NonNull android.app.Activity context, @LayoutRes int resource, @NonNull List<Business> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listBus = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.tv_EName);
        TextView tvDes = (TextView) v.findViewById(R.id.tv_EDes);
        TextView tvAddress = (TextView)v.findViewById(R.id.tv_EAddress);
        ImageView img = (ImageView) v.findViewById(R.id.imageViewEsek);
        ImageView mail = (ImageView) v.findViewById(R.id.imageViewMail);
        ImageView sms = (ImageView) v.findViewById(R.id.imageViewSms);
        ImageView phone = (ImageView) v.findViewById(R.id.imageViewPhone);
        ImageView waze = (ImageView) v.findViewById(R.id.imageViewWaze);

        mail.setImageResource(R.drawable.social_media_mail);
        sms.setImageResource(R.drawable.sms);
        phone.setImageResource(R.drawable.social_media_phone);
        waze.setImageResource(R.drawable.waze);

        tvName.setText(listBus.get(position).getBusinessName());
        tvDes.setText(listBus.get(position).getBusinessDes());
        tvAddress.setText((listBus.get(position).getBusinessAddress()));
        Glide.with(context).load(listBus.get(position).getBusinessImage()).into(img);
        Toast.makeText(context,""+tvAddress,Toast.LENGTH_LONG).show();

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mail = Uri.parse("mailto:" + listBus.get(position).getBusinessMail());
                Intent intent = new Intent(Intent.ACTION_VIEW, mail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "פניה ל" + listBus.get(position).getBusinessName());
                context.startActivity(intent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", listBus.get(position).getBusinessPhone(), null)));
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone = new Intent(Intent.ACTION_DIAL);
                phone.setData(Uri.parse("tel:" + listBus.get(position).getBusinessPhone()));
                phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(phone);

            }
        });
        waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:" + listBus.get(position).getLatitude() + "," + listBus.get(position).getLongitude();
                context.startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri)));

            }
        });

        return v;

    }

    @Override
    public int getCount() {
        return listBus.size();
    }

//    public void setFilter(ArrayList<Team> newList) {
//        listBus = new ArrayList<>();
//        listBus.addAll(newList);
//        notifyDataSetChanged();
//
//    }

}