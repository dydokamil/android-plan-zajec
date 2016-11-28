package com.example.kamil.planzajec2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;

import static com.example.kamil.planzajec2.Tools.dzienToString;

/**
 * Created by kamil on 16.11.16.
 */

public class DniActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dzien_tyg_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(DniActivity.this);

        DniInfo[] info = new DniInfo[7];
        for (int i = 1; i < 8; i++) {

            try {
                String zakres_godzin = databaseHelper.zakresGodzin(i);
                info[i - 1] = new DniInfo(dzienToString(i), zakres_godzin != null
                        && !zakres_godzin.equals("") ? zakres_godzin : "Brak zajęć");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        ListAdapter myAdapter = new DniAdapter(this, info);
        ListView myList = (ListView) findViewById(R.id.dnilist);
        myList.setAdapter(myAdapter);

        myList.setClickable(true);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DniActivity.this, MainActivity.class);
                intent.putExtra("dzien", i + 1);
                startActivity(intent);
            }
        });
    }
}
