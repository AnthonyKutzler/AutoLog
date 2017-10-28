package com.ghostlystudios.autolog.Controllers;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;
import com.ghostlystudios.autolog.Models.Kitchen;
import com.ghostlystudios.autolog.Views.Finals.IntentFinals;
import com.ghostlystudios.autolog.Views.KitchenListActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Handle Server Calls for Route Number
 */

public class LoginRequests extends Application {

    public static final int ROUTE = 0;
    public static final int KITCHEN = 1;

    Context context;
    SharedPreferences preferences;

    public LoginRequests(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Create String Request for Route number
     *
     * @return StringRequest
     */
    public StringRequest getRoute() {

        return new StringRequest(Request.Method.GET, getHostname(ROUTE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.equals("0")){
                                AlertDialog dialog = buildDialog();
                                dialog.show();
                            }else {
                                context.startActivity(new Intent(context, KitchenListActivity.class).putExtra(
                                        IntentFinals.ROUTE_NUMBER, Integer.parseInt(response)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog dialog = buildDialog();
                dialog.show();
            }
        });
    }

    public JsonArrayRequest getKitchen() {

        return new JsonArrayRequest(Request.Method.GET, getHostname(KITCHEN),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Kitchen Response", response.toString());
                try {
                    jsonToKitchenDatabase(context, response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(context, "Kitchens Updated", Toast.LENGTH_SHORT).show();
                }
                //place json array parser, and addToDB to async object    (jsonToKitchen(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() {
                //String credentials = "authentication";
                String token = ("abc");
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", token);
                return header;
            }
        };
    }

    @Nullable
    private String getHostname(int type) {
        switch (type) {
            case ROUTE:
                return getRouteHost();
            case KITCHEN:
                return getKitchenHost();
        }
        return null;
    }

    private String getRouteHost() {
        return "http://" + preferences.getString("server_address", "73.22.109.205:1234") +
                "/Rest/" + "RouteService/" + getDate() + preferences.getString("user_id", "qc");
    }

    private String getKitchenHost() {
        return "http://" + preferences.getString("server_address", "73.22.109.205:1234") +
                "/Rest/" + "KitchenService/kitchens/";
    }
    //for temp logs
    /*public void postTempLogs(TempLog[] logs) {
        final List<TempLog> tempObject = new ArrayList<>();
        for (final TempLog log : logs) {
            VolleyRestController.getInstance(context).addToRequestQueue(new StringRequest(Request.Method.POST,
                    hostname, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //something
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tempObject.add(log);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(DatabaseFinals.LOGS_COLUMNS[1], log.getName());
                    map.put(DatabaseFinals.LOGS_COLUMNS[2], log.getDate());
                    return map;
                }
            });
        }
    }*/

    /**
     * Get Current date as String
     *
     * @return String
     */
    private String getDate() {
        long currentDate = Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        String date = String.valueOf(currentDate);
        date = (date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8));
        return date + "/";
    }

    /**
     * Add list of Kitchens to local Database
     *
     * @param context   Parent Activity Context
     * @param jsonArray Kitchens as JSON
     */
    public void jsonToKitchenDatabase(Context context, JSONArray jsonArray) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        Type listType = new TypeToken<ArrayList<Kitchen>>() {
        }.getType();
        List<Kitchen> kitchens = new Gson().fromJson(String.valueOf(jsonArray), listType);
        //List<ContentValues> kitchens = new ArrayList<>();
        databaseAdapter.resetKitchen();
        for (Kitchen kitchen : kitchens) {
            databaseAdapter.addRow(DatabaseFinals.CUSTOMER_TABLE, kitchen.getContentValues());
        }
    }

    private AlertDialog buildDialog() {
        final EditText input = new EditText(context);
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        ));
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enter Route Number?").setTitle("Route?")
                .setView(input)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, KitchenListActivity.class).putExtra(
                        IntentFinals.ROUTE_NUMBER, Integer.parseInt(input.getText().toString())));

            }
        });
        return builder.create();
    }
}