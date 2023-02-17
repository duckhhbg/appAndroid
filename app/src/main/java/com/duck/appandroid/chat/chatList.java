package com.duck.appandroid.chat;

public class chatList {

    private String name,sdt,message,date,time;

    public chatList(String name, String sdt, String message, String date, String time) {
        this.name = name;
        this.sdt = sdt;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getSdt() {
        return sdt;
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
