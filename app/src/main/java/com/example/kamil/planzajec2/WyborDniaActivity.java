package com.example.kamil.planzajec2;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

/**
 * Created by kamil on 20.11.16.
 */

public class WyborDniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        Intent intent = new Intent(WyborDniaActivity.this, MainActivity.class);
        intent.putExtra("dzien", dayOfWeek);
        startActivity(intent);
    }
}
