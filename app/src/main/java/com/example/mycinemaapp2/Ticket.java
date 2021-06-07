package com.example.mycinemaapp2;

public class Ticket {

    private String titleFromTicket;
    private String timeFromTicket;
    private String placeTicket;


    public Ticket(String titleFromTicket, String timeFromTicket, String placeTicket) {
        this.titleFromTicket = titleFromTicket;
        this.timeFromTicket = timeFromTicket;
        this.placeTicket = placeTicket;
    }

    public Ticket(){

    }

    public String getTimeFromTicket() {
        return timeFromTicket;
    }

    public void setTimeFromTicket(String timeFromTicket) {
        this.timeFromTicket = timeFromTicket;
    }

    public String getTitleFromTicket() {
        return titleFromTicket;
    }

    public void setTitleFromTicket(String titleFromTicket) {
        this.titleFromTicket = titleFromTicket;
    }

    public String getPlaceTicket() {
        return placeTicket;
    }

    public void setPlaceTicket(String placeTicket) {
        this.placeTicket = placeTicket;
    }
}
