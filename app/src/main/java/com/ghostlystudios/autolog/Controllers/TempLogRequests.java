package com.ghostlystudios.autolog.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Requests to send logs
 */

public class TempLogRequests {

    private Context context;
    private SharedPreferences preferences;

    public TempLogRequests(Context context){
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getHostname(){
        return "http://" + preferences.getString("server_address", "73.22.109.205:1234") +
                "/Rest/" + "LogService/postLogs";
    }
    public JsonObjectRequest setLogs(final Map<String,String> logMap){

        return new JsonObjectRequest(Request.Method.POST, getHostname(), new JSONObject(logMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Upload Complete!", Toast.LENGTH_SHORT).show();
                        new DatabaseAdapter(context).resetLogs();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            //TODO: setup Rest to handle params
                @Override
                protected Map<String, String> getParams () {
                    return logMap;
                }
            };
    }
}
