package com.ghostlystudios.autolog.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.ghostlystudios.autolog.Controllers.DatabaseAdapter;
import com.ghostlystudios.autolog.Models.Finals.DatabaseFinals;
import com.ghostlystudios.autolog.Models.Finals.Globals;
import com.ghostlystudios.autolog.R;
import com.ghostlystudios.autolog.Views.BuildRouteLog;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * OnGeoFenceTrigger
 */

public class LoggingServices extends IntentService {

    private int routeNumber;
    private PowerManager.WakeLock wakeLock;

    public LoggingServices(){
        super("SomeName");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
            routeNumber = intent.getExtras().getInt(Globals.ROUTENUMBER);
        else
            routeNumber = 0;
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
                System.out.println(event.getErrorCode());
            }

            List<String> geoFenceIds = new ArrayList<>();
            for (Geofence fence : event.getTriggeringGeofences()) {
                geoFenceIds.add(fence.getRequestId());
            }
            wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");

            fenceLogging(geoFenceIds);
        }
    }

    private void fenceLogging(List<String> fences){
        Notification.Builder notification;
        NotificationManager notificationManager;
        Intent logIntent;
        PendingIntent pendingIntent;
        wakeLock.acquire();
        if(fences.size() > 0) {
            String time = new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", fences.get(0));
            contentValues.put("time", time);
            new DatabaseAdapter(this).addRow(DatabaseFinals.LOGS_TABLE, contentValues);
            logIntent = new Intent(this, BuildRouteLog.class).putExtra(Globals.ROUTENUMBER, routeNumber);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, logIntent, 0);
            notification = new Notification.Builder(this).setContentIntent(pendingIntent).
                    setContentTitle("Last Time Log").setSmallIcon(R.drawable.temp_log_notification_icon).
                    setContentText(fences.get(0));
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification.build());
        }
        wakeLock.release();
    }
}
