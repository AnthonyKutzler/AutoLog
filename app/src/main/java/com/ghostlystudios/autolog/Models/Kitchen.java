package com.ghostlystudios.autolog.Models;

import android.content.ContentValues;

import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;

/**
 * Stop/Kitchen Object
 */

public class Kitchen {
    private String name, address, note;
    private int routeNumber, stopNumber;
    private double xCordinate, yCordinate;

    public Kitchen(){
        super();
    }
    public Kitchen(String name, String address){
        this.name = name;
        this.address = address;
    }
    public Kitchen(String name, double yCordinate){
        this.name = name;
        this.yCordinate = yCordinate;
    }
    public Kitchen(String name, int stopNumber, String address){
        this.name = name;
        this.stopNumber = stopNumber;
        this.address = address;
    }
    public Kitchen(String name, int routeNumber, String address, String note, int stopNumber,
                   double xCordinate, double yCordinate){
        this.name = name;
        this.routeNumber = routeNumber;
        this.address = address;
        this.note = note;
        this.stopNumber = stopNumber;
        this.xCordinate = xCordinate;
        this.yCordinate = yCordinate;
    }

    public boolean exists(){
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public int getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }

    public double getxCordinate() {
        return xCordinate;
    }

    public void setxCordinate(double xCordinate) {
        this.xCordinate = xCordinate;
    }

    public double getyCordinate() {
        return yCordinate;
    }

    public void setyCordinate(double yCordinate) {
        this.yCordinate = yCordinate;
    }
    //rtenum addy note stop x y
    public ContentValues getContentValues(){
        ContentValues kitchen = new ContentValues();
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[0], name);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[1], routeNumber);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[2], address);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[3], note);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[4], stopNumber);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[5], xCordinate);
        kitchen.put(DatabaseFinals.CUSTOMER_COLUMNS[6], yCordinate);
        return kitchen;
    }
}

