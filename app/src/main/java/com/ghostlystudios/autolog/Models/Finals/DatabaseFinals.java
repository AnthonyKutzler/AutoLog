package com.ghostlystudios.autolog.Models.Finals;

/**
 * Query Finals
 */

public interface DatabaseFinals {
    //DATABASE INFO
    String DB_NAME = "delivery.db";
    int DB_VERSION = 2;

    String CUSTOMER_TABLE = "customers";
    String LOGS_TABLE = "logs";

    //Create Tables SQL Query
    String CREATE_CUSTOMER_QUERY = "CREATE TABLE customers ( name TEXT PRIMARY KEY, routeNumber INTEGER, " +
            "address TEXT, note TEXT, stopNumber INTEGER, xCordinate REAL, yCordinate REAL)";
    String CREATE_LOGS_QUERY = "CREATE TABLE logs ( name TEXT PRIMARY KEY, time TEXT)";

    //Column Finals
    String[] CUSTOMER_COLUMNS = {"name", "routeNumber", "address", "note", "stopNumber",
            "xCordinate", "yCordinate"};
    String[] CUSTOMER_LIST_COLUMNS = {CUSTOMER_COLUMNS[0], CUSTOMER_COLUMNS[2]};
    String[] LOGS_COLUMNS = {"name", "date"};

    //Drop Tables
    String DROP_CUSTOMERS = "DROP TABLE IF EXISTS customers";
    String DROP_LOGS = "DROP TABLE IF EXISTS logs";

    //SELECT *
    String SELECT_LOGS = "SELECT * FROM logs";
}
