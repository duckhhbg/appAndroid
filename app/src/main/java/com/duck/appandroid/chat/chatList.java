package com.duck.appandroid.chat;

public class chatList {

    private String sdt,name,message,date,time;

    public chatList(String sdt, String name, String message, String date, String time) {

        this.sdt = sdt;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getSdt() {
        return sdt;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
