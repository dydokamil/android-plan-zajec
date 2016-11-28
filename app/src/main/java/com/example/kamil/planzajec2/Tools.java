package com.example.kamil.planzajec2;

/**
 * Created by kamil on 20.11.16.
 */

public class Tools {
    public static String dzienToString(int i){
        switch (i) {
            case 1:
                return "Niedziela";
            case 2:
                return "Poniedziałek";
            case 3:
                return "Wtorek";
            case 4:
                return "Środa";
            case 5:
                return "Czwartek";
            case 6:
                return "Piątek";
            case 7:
                return "Sobota";
            default:
                return "???";
        }
    }
}
