package com.ghostlystudios.autolog.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;

/**
 * Database Handler for KitchenInformation and Temp Logs
 */

public class DatabaseModel extends SQLiteOpenHelper {


    //Possibly UPDATE?
    public DatabaseModel(Context context) {
        super(context, DatabaseFinals.DB_NAME, null, DatabaseFinals.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseFinals.CREATE_CUSTOMER_QUERY);
        db.execSQL(DatabaseFinals.CREATE_LOGS_QUERY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseFinals.DROP_CUSTOMERS);
        db.execSQL(DatabaseFinals.DROP_LOGS);
        this.onCreate(db);
    }

    // Getters
    public SQLiteDatabase getReadable(){
        return getReadableDatabase();
    }
    public SQLiteDatabase getWritable(){
        return getWritableDatabase();
    }
    //--------------------------CRUD -----------------
    //-----------------ADD ROW----------------

}
