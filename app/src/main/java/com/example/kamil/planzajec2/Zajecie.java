package com.example.kamil.planzajec2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Zajecie {
    private int id;
    private int dzien;
    private String przedmiot;
    private String godzina;
    private String godzina_end;

    public String getGodzina_end() {
        return godzina_end;
    }

    private String sala;
    int stan;

    public void setStan(int stan) {
        this.stan = stan;
    }

    public int getStan() {
        return stan;
    }

    public Zajecie(){}

    public Zajecie(int dzien, String przedmiot, String godzina, String sala) {
        this.id = -1;
        this.dzien = dzien;
        this.przedmiot = przedmiot;
        this.godzina = godzina;
        this.sala = sala;
    }

    public Zajecie(int id, int dzien, String przedmiot, String godzina, String sala) {
        this.id = id;
        this.dzien = dzien;
        this.przedmiot = przedmiot;
        this.godzina = godzina;
        this.sala = sala;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDzien() {
        return dzien;
    }

    public String getGodzina() {
        return godzina;
    }

    public String getGodzinaFull() {
        return godzina + " - " + godzina_end;
    }

    public String getPrzedmiot() {
        return przedmiot;
    }

    public String getSala() {
        return sala;
    }

    public String getSalaFull() {
        return "Sala: " + sala;
    }

    public void setDzien(int dzien) {
        this.dzien = dzien;
    }

    public static String dodajMinuty(String godzina, int minuty) throws ParseException {

        if (godzina == null)
            return null;

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = df.parse(godzina);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 90);

        return df.format(cal.getTime());

    }

    public void setGodzina(String godzina) throws ParseException {

        this.godzina = godzina;
        this.godzina_end = dodajMinuty(godzina, 90);

    }

    public void setPrzedmiot(String przedmiot) {
        this.przedmiot = przedmiot;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
}
