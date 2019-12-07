package com.example.todolist.sharecalendar;

public class Sh_Events {
    String EVENT, TIME, DATE, MONTH, YEAR;
//    int LOCALE;

    public Sh_Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR/*, int LOCALE*/) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
//        this.LOCALE = LOCALE;
    }

    public String getEVENT() {
        return EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

//    public int getLOCALE() {return LOCALE;}
}
