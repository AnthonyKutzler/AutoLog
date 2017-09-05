package com.ghostlystudios.autolog.Models;

import android.content.ContentValues;


import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;

import java.util.HashMap;
import java.util.Map;

/**
 * TempLog Object
 */

public class TempLog {
    private String name, date;

    public TempLog(String name, String date){
        super();
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseFinals.LOGS_COLUMNS[1], name);
        cv.put(DatabaseFinals.LOGS_COLUMNS[2], date);
        return cv;
    }
    public Map<String, String> getHashMap(TempLog[] logs){
        Map<String, String> map = new HashMap<>();
        for(TempLog log: logs){
            map.put(DatabaseFinals.LOGS_COLUMNS[1], log.getName());
            map.put(DatabaseFinals.LOGS_COLUMNS[2] , log.getDate());
        }
        return map;
    }
}
