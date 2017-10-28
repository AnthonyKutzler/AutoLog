package com.ghostlystudios.autolog.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ghostlystudios.autolog.Models.DatabaseModel;
import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;
import com.ghostlystudios.autolog.Models.Kitchen;
import com.ghostlystudios.autolog.Models.TempLog;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Adapter
 * For Controlling All things Database Related
 */

public class DatabaseAdapter {


    private DatabaseModel databaseModel;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        databaseModel = new DatabaseModel(context);
    }
    /*
    public void clearTable(String tableName){
        database = databaseModel.getWritable();
        database.execSQL(DatabaseFinals.DROP_LOGS);
        database.execSQL(DatabaseFinals.CREATE_LOGS_QUERY);
        database.close();
    }*/
    //Generic Add Row/Rows

    /**
     * Generic Add Rows
      * @param tableName String
     * @param values ContentValues
     */
    public void addRow(String tableName, ContentValues values){
        database = databaseModel.getWritable();
        try {
            database.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }catch(Exception e){
            e.printStackTrace();
        }
            database.close();
    }
    /*public void addRows(String tableName, List<ContentValues> values){
        database = databaseModel.getWritable();
        for(ContentValues value : values){
            database.insert(tableName, null, value);
        }
        database.close();
    }*/

    //Reset Kitchen SQL
    void resetKitchen(){
        database = databaseModel.getWritable();
        database.execSQL(DatabaseFinals.DROP_CUSTOMERS);
        database.execSQL(DatabaseFinals.CREATE_CUSTOMER_QUERY);
    }
    //Reset TempLogs SQL
    public void resetLogs(){
        database = databaseModel.getWritable();
        database.execSQL(DatabaseFinals.DROP_LOGS);
        database.execSQL(DatabaseFinals.CREATE_LOGS_QUERY);
    }
    //------------------------GET ROW-------------------

    /**
     *  Get all Kitchens on a Route
     * @param routeNumber int
     * @param selectColumns String[]
     * @return List<Kitchen>
     */
    List<Kitchen> getKitchensByRouteNumber(int routeNumber, @Nullable String[] selectColumns){
        if(selectColumns == null)
            selectColumns = new String[] {DatabaseFinals.CUSTOMER_COLUMNS[0], DatabaseFinals.CUSTOMER_COLUMNS[2]};
        Cursor cursor;
        database  = databaseModel.getReadable();

        //if number = 0 list all routes
        if(routeNumber == 0)
            cursor = database.query(DatabaseFinals.CUSTOMER_TABLE, selectColumns
                    , " routeNumber > ?",
                    new String[] {Integer.toString(routeNumber)}, null, null, DatabaseFinals.CUSTOMER_COLUMNS[4]);
        else
            cursor = database.query(DatabaseFinals.CUSTOMER_TABLE, selectColumns
                    , " routeNumber = ?",
                    new String[] {Integer.toString(routeNumber)}, null, null, DatabaseFinals.CUSTOMER_COLUMNS[4]);


        List<Kitchen> kitchens = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                kitchens.add(new Kitchen(cursor.getString(0), cursor.getString(1)));
            }while (cursor.moveToNext());
        }else
            kitchens.add(new Kitchen("1", "2"));

        cursor.close();
        database.close();
        return kitchens;
    }

    /**
     * Get all kitchens for Cordinates
     * @param routeNumber int
     * @return HashMap
     */
    public Map<Double, Kitchen> getKitchenMap(int routeNumber) {
        Map<Double, Kitchen> doubleKitchenInfoMap = new HashMap<>();
        database = databaseModel.getReadable();
        String[] columns = {DatabaseFinals.CUSTOMER_COLUMNS[0], DatabaseFinals.CUSTOMER_COLUMNS[5],
                DatabaseFinals.CUSTOMER_COLUMNS[6]};
        Cursor cursor = database.query(DatabaseFinals.CUSTOMER_TABLE, columns
                , " routeNumber = ?",
                new String[]{Integer.toString(routeNumber)}, null, null, DatabaseFinals.CUSTOMER_COLUMNS[4]);
        if (cursor.moveToFirst()) {
            do {
                doubleKitchenInfoMap.put(cursor.getDouble(1), new Kitchen(cursor.getString(0),
                        cursor.getDouble(2)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return doubleKitchenInfoMap;
    }

    public List<Geofence> getKitchenFence(int routeNumber){
        List<Geofence> geofences = new ArrayList<>();
        database = databaseModel.getReadable();
        String[] columns = {DatabaseFinals.CUSTOMER_COLUMNS[0], DatabaseFinals.CUSTOMER_COLUMNS[5],
                DatabaseFinals.CUSTOMER_COLUMNS[6]};
        Cursor cursor;
        if(routeNumber != 0)
            cursor = database.query(DatabaseFinals.CUSTOMER_TABLE, columns
                , " routeNumber = ?",
                new String[]{Integer.toString(routeNumber)}, null, null, DatabaseFinals.CUSTOMER_COLUMNS[4]);
        else
            cursor = database.query(DatabaseFinals.CUSTOMER_TABLE, columns
                    , " routeNumber > ?",
                    new String[]{Integer.toString(routeNumber)}, null, null, DatabaseFinals.CUSTOMER_COLUMNS[4]);
        if (cursor.moveToFirst()) {
            do {
                geofences.add(new Geofence.Builder().setRequestId(cursor.getString(0))
                        .setCircularRegion(cursor.getDouble(1), cursor.getDouble(2), 200)
                        //TODO: set radius programmatically, 1000 is ~ 2/3 of a mile// Now 200
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                        .setLoiteringDelay(1000*30)
                        //TODO: Set LoiteringDelay Higher, 30secs is to Short, maybe 60,90, 120?
                        .setExpirationDuration(1000*60*60*12).build());
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return geofences;

    }
    /***
     * Get all rows of TempLog
     * @return TempLog[]
     */
    public List<TempLog> getTempLogs(){
        database = databaseModel.getReadable();
        List<TempLog> logList = new ArrayList<>();
        Cursor cursor = database.rawQuery(DatabaseFinals.SELECT_LOGS, null);
        if(cursor.moveToFirst()){
            do{
                logList.add(new TempLog(cursor.getString(0), cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return logList;
    }
    public Map<String, String> getTempLogsMap(){
        database = databaseModel.getReadable();
        Map<String, String> logMap = new HashMap<>();
        Cursor cursor = database.rawQuery(DatabaseFinals.SELECT_LOGS, null);
        if(cursor.moveToFirst()){
            do{
                logMap.put(cursor.getString(1), cursor.getString(2));
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return logMap;
    }
}
