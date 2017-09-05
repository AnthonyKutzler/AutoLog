package com.ghostlystudios.autolog.Controllers;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Handle Network RequestQueue
 */

public class VolleyRestController extends Application{

    private static VolleyRestController instance;
    private Context context;
    public static final String TAG = VolleyRestController.class.getSimpleName();

    private RequestQueue requestQueue;


    public VolleyRestController(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
        //requestQueue.start();
    }
    public static synchronized VolleyRestController getInstance(Context context){
        if(instance == null)
            instance = new VolleyRestController(context);
        return instance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
        //requestQueue.start();
    }

}
