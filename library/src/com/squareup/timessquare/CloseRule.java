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

    private Set<Integer> mCloseRules = new HashSet<Integer>();
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
        if (dates == null) {
            return this;
        }

        for (Date d : dates) {
            mCloseRules.add(encode(d));
        }
        return this;
    }

    private boolean in(int i, int[] ia) {
        if (ia == null) {
            return false;
        }

        for (int j : ia) {
            if (i == j) {
                return true;
            }
        }
        return false;
    }

    public CloseRule withOpenWeekdays(int[] weekdays) {
        if (weekdays == null) {
            return this;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)mFrom.clone());
        while (!sameDate(cal.getTime(), mTo)) {
            if (!in(cal.get(Calendar.DAY_OF_WEEK), weekdays)) {
                mCloseRules.add(encode(cal.getTime()));
            } else {
                mCloseRules.remove(encode(cal.getTime()));
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
                mCloseRules.add(encode(cal.getTime()));
            } else {
                mCloseRules.remove(encode(cal.getTime()));
            }
            cal.add(Calendar.DATE, 1);
        }
        return this;
    }

    /*
     * Must be called after openWeekday and openMonth. It's used
     * to filter from the already chosen close rules.
     *
     * TODO Currently editing.
     */
    public CloseRule withPreorder(int pre, Date time) {
        if (pre == 0) {
            //When preorder only start from today.
            Date now = new Date();
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);

            Calendar order = Calendar.getInstance();
            order.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            order.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            if (order.getTime().after(now)) {
                mCloseRules.remove(encode(now));
            } else {
                mCloseRules.add(encode(now));
            }
        } else {
            //Handle invalidate days.
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < pre; i ++) {
                mCloseRules.add(encode(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }

            //Handle today.
            if (afterTime(cal.getTime(), time)) {
                mCloseRules.add(encode(cal.getTime()));
            }
        }

        return this;
    }

    private boolean afterTime(Date a, Date b) {
        a.setYear(0);
        a.setMonth(0);
        a.setDate(0);

        b.setYear(0);
        b.setMonth(0);
        b.setDate(0);

        return a.getTime() > b.getTime();
    }

    private void preorderToday() {
        
    }

    public Set<Integer> build() {
        return mCloseRules;
    }
}

