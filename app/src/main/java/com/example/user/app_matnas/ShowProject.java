package com.example.user.app_matnas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowProject extends AppCompatActivity {

    private Project project;
    private Toolbar toolbar;
    private TextView toolBarText;
    private TextView des;
    private ImageView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        project = (Project) getIntent().getSerializableExtra("show");
        toolBarText.setText(project.getProjectName());

        des = (TextView) findViewById(R.id.tv_PDes);
        logo = (ImageView) findViewById(R.id.imageViewProject);
        fillLayout();
        }



    private void fillLayout()
    {
        des.setText(project.getProjectDes());
        Glide.with(getApplicationContext()).load(project.getProjectLogo()).into(logo);


    }


}
