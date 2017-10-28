package com.ghostlystudios.autolog.Views;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ghostlystudios.autolog.Controllers.LoginRequests;
import com.ghostlystudios.autolog.Controllers.VolleyRestController;
import com.ghostlystudios.autolog.R;
import com.ghostlystudios.autolog.Services.LoggingServices;
import com.google.android.gms.location.LocationServices;

//TODO: ADD Token Authentication to RestService and Client
public class Login extends AppCompatActivity implements OnClickListener
{

    Button buttonLogin;
    LoginRequests loginRequests;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginxm);
        //Request Permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK}, 1);
        }

        //Initialize Listener on Button
        (buttonLogin = (Button) findViewById(R.id.btnlogin)).setOnClickListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        loginRequests = new LoginRequests(this);
        requestQueue = Volley.newRequestQueue(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        switch (i.getItemId()) {
            case R.id.refresh:
                grabKitchens();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingFrag.class));
        }
        return super.onOptionsItemSelected(i);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:
                grabRoute();
                break;
        }
    }

    private void grabKitchens() {
        VolleyRestController.getInstance(this).addToRequestQueue(loginRequests.getKitchen());
    }

    private void grabRoute() {
        VolleyRestController.getInstance(this).addToRequestQueue(loginRequests.getRoute());
    }

}