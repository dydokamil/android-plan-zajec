package com.example.kamil.planzajec2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kamil on 15.11.16.
 */

class CustomAdapter extends ArrayAdapter<Zajecie> {


    public CustomAdapter(Context context, Zajecie[] resource) {
        super(context, R.layout.zajecia_row_green, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = null;

        Zajecie zajecie = getItem(position);

        String start = zajecie != null ? zajecie.getGodzina() : null;
        Date time1 = null;
        try {
            time1 = new SimpleDateFormat("HH:mm").parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);


        String end = zajecie != null ? zajecie.getGodzina_end() : null;
        Date time2 = null;
        try {
            time2 = new SimpleDateFormat("HH:mm").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        Date time3 = null;
        try {
            time3 = new SimpleDateFormat("HH:mm").parse("" + hour + ":" + minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(time3);

        Date poczatek = calendar1.getTime();
        Date koniec = calendar2.getTime();
        Date now = calendar3.getTime();

        if (zajecie != null) {
            if (now.after(poczatek) && now.after(koniec)) {
                zajecie.stan = 0;
            } else if (now.after(poczatek) && now.before(koniec)
                    || now.equals(poczatek)
                    || now.equals(koniec)) {
                zajecie.stan = 1;
            } else {
                zajecie.stan = 2;
            }
        }

        switch (zajecie != null ? zajecie.stan : 0) {
            case 0:
                customView = inflater.inflate(R.layout.zajecia_row_gray, parent, false);
                break;
            case 1:
                customView = inflater.inflate(R.layout.zajecia_row_green, parent, false);
                break;
            case 2:
                customView = inflater.inflate(R.layout.zajecia_row_blue, parent, false);
                break;
        }

        TextView nazwa_tv = (TextView) (customView != null ? customView.findViewById(R.id.nazwa) : null);
        TextView godzina_tv = (TextView) (customView != null ? customView.findViewById(R.id.godzina) : null);
        TextView sala_tv = (TextView) (customView != null ? customView.findViewById(R.id.sala) : null);

        if (nazwa_tv != null) {
            nazwa_tv.setText(zajecie != null ? zajecie.getPrzedmiot() : null);
        }
        if (godzina_tv != null) {
            godzina_tv.setText(zajecie != null ? zajecie.getGodzinaFull() : null);
        }
        if (sala_tv != null) {
            sala_tv.setText(zajecie != null ? zajecie.getSalaFull() : null);
        }

        return customView;

    }
}
