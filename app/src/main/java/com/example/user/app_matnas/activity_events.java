package com.example.user.app_matnas;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;


public class activity_events extends AppCompatActivity
{
    CalendarView cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        cal = (CalendarView)findViewById(R.id.calendarView);
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.setMaxDate(daysInMonth);
        }



    }
}