package com.example.kamil.planzajec2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import static com.example.kamil.planzajec2.Tools.dzienToString;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    Button btn_dodaj;
    DatabaseHelper databaseHelper;

    ListView uzupelnijListeZajec(Toolbar toolbar, int dzien) {

        // pobierz zajecia na dany dzien //
        List zajecia = null;
        try {
            zajecia = databaseHelper.getZajecia(dzien);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert zajecia != null;
        Zajecie[] zajecia_arr = new Zajecie[zajecia.size()];

        for (int j = 0; j < zajecia.size(); j++) {
            zajecia_arr[j] = (Zajecie) zajecia.get(j);
        }

        toolbar.setTitle("" + dzienToString(dzien));

        ListAdapter myAdapter = new CustomAdapter(this, zajecia_arr);
        ListView myList = (ListView) findViewById(R.id.listzajecia);
        myList.setAdapter(myAdapter);

        return myList;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        Bundle b = getIntent().getExtras();
        final int dzien = b.getInt("dzien");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView myList = uzupelnijListeZajec(toolbar, dzien);

        myList.setClickable(true);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Zajecie zajecie_item = (Zajecie) adapterView.getItemAtPosition(i);

                final Dialog dialog = new Dialog(MainActivity.this,
                        R.style.AppCompatAlertDialogStyle);
                dialog.setTitle("Dodawanie zajecia");
                dialog.setContentView(R.layout.edycja);
                dialog.show();

                final EditText nazwa_zajec = (EditText) dialog.findViewById(R.id.nazwa_zajec);
                final EditText sala = (EditText) dialog.findViewById(R.id.sala_tf);
                final TextView godzina_tf = (EditText) dialog.findViewById(R.id.godzina_tf);

                Button btn_edytuj = (Button) dialog.findViewById(R.id.button_edytuj);
                Button btn_cancel = (Button) dialog.findViewById(R.id.button_cancel);
                Button btn_usun = (Button) dialog.findViewById(R.id.btn_usun);

                nazwa_zajec.setText(zajecie_item.getPrzedmiot());
                sala.setText(zajecie_item.getSala());
                godzina_tf.setText(zajecie_item.getGodzina());

                // przycisk edytuj
                btn_edytuj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (godzina_tf.getText().toString().contains(":")
                                && !nazwa_zajec.getText().toString().equals("")
                                && !sala.getText().toString().equals("")) { // poprawnie

                            zajecie_item.setSala(sala.getText().toString());
                            zajecie_item.setPrzedmiot(nazwa_zajec.getText().toString());
                            try {
                                zajecie_item.setGodzina(godzina_tf.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            databaseHelper.updateZajecie(zajecie_item);
                            dialog.cancel();

                            uzupelnijListeZajec(toolbar, dzien);

                        } else { // niepoprawnie

                            Toast.makeText(MainActivity.this, "Niepoprawne argumenty!",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                btn_usun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseHelper.deleteZajecie(zajecie_item);
                        dialog.cancel();

                        uzupelnijListeZajec(toolbar, dzien);
                    }
                });

                // kliknieto editText godziny
                godzina_tf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar mCurrentTime = Calendar.getInstance();
                        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mCurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        godzina_tf.setText(i + ":" + i1);
                                    }
                                }, hour, minute, true);
                        mTimePicker.setTitle("Wybierz czas rozpoczecia");
                        mTimePicker.show();
                    }
                });

                // kliknieto editText nazwy
                nazwa_zajec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nazwa_zajec.setText("");
                    }
                });

                // kliknieto editText sali
                sala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sala.setText("");
                    }
                });

                // kliknieto przycisk zamknij dnialog
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // tworzenie dialogu
                final Dialog dialog = new Dialog(MainActivity.this,
                        R.style.AppCompatAlertDialogStyle);
                dialog.setTitle("Dodawanie zajecia");
                dialog.setContentView(R.layout.dodawanie);
                dialog.show();

                final EditText nazwa_zajec = (EditText) dialog.findViewById(R.id.nazwa_zajec);
                final EditText sala = (EditText) dialog.findViewById(R.id.sala_tf);
                final TextView godzina_tf = (EditText) dialog.findViewById(R.id.godzina_tf);

                Button btn_dodaj = (Button) dialog.findViewById(R.id.button_dodaj);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                // przycisk dodaj
                btn_dodaj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (godzina_tf.getText().toString().contains(":")
                                && !nazwa_zajec.getText().toString().equals("")
                                && !sala.getText().toString().equals("")) { // poprawnie

                            databaseHelper.addZajecie(new Zajecie(dzien,
                                    nazwa_zajec.getText().toString(),
                                    godzina_tf.getText().toString(),
                                    sala.getText().toString()));

                            dialog.cancel();

                            uzupelnijListeZajec(toolbar, dzien);

                        } else { // niepoprawnie

                            Toast.makeText(MainActivity.this, "Niepoprawne argumenty!",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                // kliknieto editText godziny
                godzina_tf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar mCurrentTime = Calendar.getInstance();
                        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mCurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        godzina_tf.setText(i + ":" + (i1 < 10 ? "0" + i1 : i1));
                                    }
                                }, hour, minute, true);
                        mTimePicker.setTitle("Wybierz czas rozpoczecia");
                        mTimePicker.show();
                    }
                });

                // kliknieto editText nazwy
                nazwa_zajec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nazwa_zajec.setText("");
                    }
                });

                // kliknieto editText sali
                sala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sala.setText("");
                    }
                });

                // kliknieto przycisk zamknij dnialog
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DniActivity.class);
                startActivity(i);
            }
        });
    }

}
