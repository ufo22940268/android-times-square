package com.squareup.timessquare.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

import com.squareup.timessquare.CloseRule;
import static android.widget.Toast.LENGTH_SHORT;

public class SampleTimesSquareActivity extends Activity {
    private static final String TAG = "SampleTimesSquareActivity";
    private CalendarPickerView calendar;

    private Date d1 = getDate(1);
    private Date d2 = getDate(2);
    private Date d20 = getDate(20);
    private Date d3 = getDate(3);
    private Date d4 = getDate(4);
    private Date d5 = getDate(5);
    private Date d6 = getDate(6);
    private Date d8 = getDate(8);
    private Date d26 = getDate(26);
    private Date d27 = getDate(27);
    private Date d28 = getDate(28);
    private Date d31 = getDate(31);

    private static Date getDate(int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, date);
        return cal.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_calendar_picker);

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 2);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        final Button disable_dates = (Button) findViewById(R.id.button_disable_dates);
        final Button preorder = (Button) findViewById(R.id.button_preorder);
        final Button weekly = (Button) findViewById(R.id.button_weekly);
        final Button month = (Button) findViewById(R.id.button_month);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        disable_dates.setEnabled(true);
        preorder.setEnabled(true);
        weekly.setEnabled(true);

        final Calendar today = Calendar.getInstance();
        final ArrayList<Date> dates = new ArrayList<Date>();

        Collection<Integer> rules = CloseRule.init(d1, d31)
            .withDisableDates(Arrays.asList(d26, d27))
            .build();

        calendar.init(d1, d31, rules); //
        calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            public void onDateSelected(Date date) {
                System.out.println("++++++++++++++++++++" + date + "++++++++++++++++++++");
                finish();
            }
        });


        disable_dates.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disable_dates.setEnabled(false);
                preorder.setEnabled(true);
                weekly.setEnabled(true);
                month.setEnabled(true);

                Collection<Integer> rules = CloseRule.init(d1, d31)
                    .withDisableDates(Arrays.asList(d26, d27))
                    .build();

                calendar.init(lastYear.getTime(), nextYear.getTime(), rules); //

                Toast.makeText(SampleTimesSquareActivity.this,
                    "disabled date " + d26 + "," + d27 , Toast.LENGTH_SHORT).show();
            }
        });

        preorder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disable_dates.setEnabled(true);
                preorder.setEnabled(false);
                weekly.setEnabled(true);
                month.setEnabled(true);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 6);
                Date mornning = cal.getTime();
                cal.set(Calendar.HOUR_OF_DAY, 23);
                Date night = cal.getTime();

                int pre = 0;
                Date preTime = night;
                Collection<Integer> rules = CloseRule.init(d1, d31)
                    .withPreorder(pre, preTime)
                    .build();

                calendar.init(d1, d31, rules);

                Toast.makeText(SampleTimesSquareActivity.this,
                    String.format("preorder: %d days before %s", pre, preTime.toString()) , Toast.LENGTH_SHORT).show();
            }
        });

        weekly.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disable_dates.setEnabled(true);
                preorder.setEnabled(true);
                weekly.setEnabled(false);
                month.setEnabled(true);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                Date prevMonth = cal.getTime(); 
                cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 1);
                Date nextMonth = cal.getTime(); 

                Collection<Integer> rules = CloseRule.init(prevMonth, nextMonth)
                    .withOpenWeekday(Calendar.MONDAY)
                    .build();

                calendar.init(prevMonth, nextMonth, rules); //
                Toast.makeText(SampleTimesSquareActivity.this,
                    "Open on monday", Toast.LENGTH_SHORT).show();
            }
        });

        month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disable_dates.setEnabled(true);
                preorder.setEnabled(true);
                weekly.setEnabled(true);
                month.setEnabled(false);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                Date prevMonth = cal.getTime(); 
                cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 4);
                Date nextMonth = cal.getTime(); 

                Collection<Integer> rules = CloseRule.init(prevMonth, nextMonth)
                    .withOpenMonth(Calendar.AUGUST)
                    .build();

                calendar.init(prevMonth, nextMonth, rules); //

                Toast.makeText(SampleTimesSquareActivity.this,
                    "Open on august", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
