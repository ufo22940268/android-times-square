package com.squareup.timessquare;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.net.*;
import android.opengl.*;
import android.graphics.*;
import android.view.animation.*;
import android.text.TextUtils;

import java.util.*;

import org.json.*;

public class CloseRule {

    private Set<Integer> mRules = new HashSet<Integer>();
    private Date mFrom;
    private Date mTo;

    public static CloseRule init(Date f, Date t) {
        CloseRule r =  new CloseRule();
        r.mFrom = f;
        r.mTo = t;
        return r;
    }

    public static List<Date> getDateRules() {
        List<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance(); 
        Date from = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date to = cal.getTime();

        cal.setTime(from);
        while(cal.getTime().before(to)) {
            dates.add(cal.getTime());
            cal.add(Calendar.DATE, 2);
        }
        return dates;
    }

    public static List<Integer> getRules() {
        List<Integer> ints = new ArrayList<Integer>();
        for (Date d : getDateRules()) {
            ints.add(encode(d));
        }
        return ints;
    }

    public static int encode(Date date) {
        int r = 0;
        r =  (r + date.getYear())*10000;
        r =  (r + date.getMonth())*100;
        r =  (r + date.getDate())*100;
        return r;
    }

    public static boolean inRules(Date d, Collection<Integer> rules) {
        return rules.contains(encode(d));
    }

    private static boolean sameDate(Date d1, Date d2) {
        return d1.getMonth() == d2.getMonth() 
            && d1.getDate() == d2.getDate() 
            && d1.getYear() == d2.getYear();
    }

    public CloseRule withDisableDates(List<Date> dates) {
        for (Date d : dates) {
            mRules.add(encode(d));
        }
        return this;
    }

    public CloseRule withOpenWeekday(int weekday) {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)mFrom.clone());
        while (!sameDate(cal.getTime(), mTo)) {
            if (cal.get(Calendar.DAY_OF_WEEK) != weekday) {
                mRules.add(encode(cal.getTime()));
            } else {
                mRules.remove(encode(cal.getTime()));
            }
            cal.add(Calendar.DATE, 1);
        }
        return this;
    }

    public CloseRule withOpenMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)mFrom.clone());
        while (!sameDate(cal.getTime(), mTo)) {
            if (cal.get(Calendar.MONTH) != month) {
                mRules.add(encode(cal.getTime()));
            } else {
                mRules.remove(encode(cal.getTime()));
            }
            cal.add(Calendar.DATE, 1);
        }
        return this;
    }

    public Set<Integer> build() {
        return mRules;
    }
}

