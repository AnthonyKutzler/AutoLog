package com.ghostlystudios.autolog.Services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ghostlystudios.autolog.Controllers.DatabaseAdapter;
import com.ghostlystudios.autolog.Models.FallbackLocationTracker;
import com.ghostlystudios.autolog.Models.Finals.Globals;
import com.ghostlystudios.autolog.Models.ProviderLocationTracker;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.Executor;


/**
 * Add and start geoFencing
 */

public class GeofencingServices extends IntentService implements OnCompleteListener{
    //Thread/Runnable Objects
    //private Runnable handlerTask;
    //private Handler handler;
    //Data Objects
    //private DatabaseAdapter databaseAdapter;

    private int routeNumber;
    //Notification Objects
    /*Notification.Builder notification;
    Notification.InboxStyle inboxStyle;
    NotificationManager notificationManager;
    Intent logIntent;
    PendingIntent pendingIntent;
    private Map<Double, Kitchen> map;
*/  Intent loggingIntent;
    //private PendingIntent geofencingRequestIntent;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofences;
    //GPS Listener
    //CordinateManager cordinateManager;
    //TODO: Replace Cordinate Manager with below
    FallbackLocationTracker locationTracker;

    public GeofencingServices(){
        super("loggingService");
    }

    /**
     * Initialize variables need for the rest of the service
     * @param intent something something
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        locationTracker = new FallbackLocationTracker(this, ProviderLocationTracker.ProviderType.GPS);
        if(intent != null)
            routeNumber = intent.getIntExtra(Globals.ROUTENUMBER, 0);
        else
            routeNumber = 0;
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        geofences = databaseAdapter.getKitchenFence(routeNumber);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingClient.addGeofences(getGeofencingRequest(), getPendingIntent())
                .addOnCompleteListener(this);

        //map = databaseAdapter.getKitchenMap(routeNumber);
        //cordinateManager = new CordinateManager((LocationManager)this.getSystemService(Context.LOCATION_SERVICE));
        /*logIntent = new Intent(this, BuildRouteLog.class).putExtra(Globals.ROUTENUMBER, routeNumber);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, logIntent, 0);
        notification = new Notification.Builder(this).setContentIntent(pendingIntent).
                setContentTitle("Last Time Log").setSmallIcon(R.drawable.temp_log_notification_icon).
                setContentText("No Data Yet");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
        *///handler = new Handler(Looper.myLooper());
        //initHandler();
    }
    //Repeated Timer
    /*private void initHandler(){
        handlerTask = new Runnable() {
            @Override
            public void run() {

                String kitchenName = checkCordinates();
                if(kitchenName != null){
                    //Create ContentValues for DB insert
                    String time = new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", kitchenName);
                    contentValues.put("time", time);
                    databaseAdapter.addRow(DatabaseFinals.LOGS_TABLE, contentValues);
                    //Set expanded notification text
                    inboxStyle = resetInboxStyle();
                    for(TempLog log: databaseAdapter.getTempLogs()) {
                        String line = log.getName();
                        for(int z = line.length(); z <= 12; z++){
                            line += " ";
                        }
                        line += log.getDate();
                        inboxStyle.addLine(line);
                    }
                    notification.setStyle(inboxStyle);
                    //Display Notification
                    notificationManager.notify(0, notification.setContentText(
                            kitchenName + "  " + time).build());
                }
                //Reset timer
                handler.postDelayed(this, ServiceFinals.INTERVAL);
            }
        };

        //First set timer
        handler.postDelayed(handlerTask, ServiceFinals.INTERVAL);
    }*/

    @Override
    public void onDestroy(){
        geofencingClient.removeGeofences(getPendingIntent()).addOnCompleteListener(this);
        stopService(loggingIntent);
    }

    private GeofencingRequest getGeofencingRequest(){
        return new GeofencingRequest.Builder()
                .addGeofences(geofences)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .build();
    }

    private PendingIntent getPendingIntent(){

        loggingIntent = new Intent(this, LoggingServices.class).putExtra(Globals.ROUTENUMBER, routeNumber);
        return PendingIntent.getService(this, 0, loggingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if(!task.isSuccessful()){}
            //some problem
    }

    //Parse through cordinate map
    //private String checkCordinates(){
    //    for(Map.Entry<Double, Kitchen> entry : map.entrySet()){
    /*        if(isInBox(entry.getKey(), 0)){
                //double lng = entry.getValue().getYCordinate();
                if(isInBox(entry.getValue().getYCordinate(), 1)){
                    String name = (entry.getValue()).getName();
                    map.remove((entry.getKey()));
                    return name;
                }
            }
        }
        return null;
    }
    private Notification.InboxStyle resetInboxStyle(){
        return new Notification.InboxStyle().setBigContentTitle("Recent Time Logs");
    }

    //Check if current cordinates are close enough to log(i.e. return true)
    private boolean isInBox(double checkAxis, int axis){
        if(axis == 0) {
            double lat = cordinateManager.getLat();
            return checkAxis + ServiceFinals.OFFSET > lat &&
                    checkAxis - ServiceFinals.OFFSET < lat;
        }
        else if(axis == 1) {
            double lng = cordinateManager.getLon();
            return checkAxis + ServiceFinals.OFFSET > lng &&
                    checkAxis - ServiceFinals.OFFSET < lng;
        }
        else
            return false;
    }*/
}
