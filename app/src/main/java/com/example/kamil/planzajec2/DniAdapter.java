package com.example.kamil.planzajec2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by kamil on 15.11.16.
 */

class DniInfo {

    DniInfo(String nazwa, String godziny) {

        this.godziny_zajec = godziny;
        this.nazwa_dnia = nazwa;

    }

    String nazwa_dnia;
    String godziny_zajec;

}

class DniAdapter extends ArrayAdapter<DniInfo> {


    public DniAdapter(Context context, DniInfo[] resource) {
        super(context, R.layout.dzien_tyg_item, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dzien_tyg_item, parent, false);

        DniInfo dzien = getItem(position);

        TextView nazwa_tv = (TextView) customView.findViewById(R.id.nazwa_dnia);
        TextView godzina_tv = (TextView) customView.findViewById(R.id.godziny_zajec);

        assert dzien != null;
        nazwa_tv.setText(dzien.nazwa_dnia);
        godzina_tv.setText(dzien.godziny_zajec);

        return customView;

    }
}
