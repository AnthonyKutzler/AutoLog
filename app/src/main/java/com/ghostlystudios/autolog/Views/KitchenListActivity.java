package com.ghostlystudios.autolog.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ghostlystudios.autolog.Controllers.ClickListener;
import com.ghostlystudios.autolog.Controllers.DatabaseAdapter;
import com.ghostlystudios.autolog.Controllers.KitchenController;
import com.ghostlystudios.autolog.Controllers.KitchenAdapter;
import com.ghostlystudios.autolog.Models.*;
import com.ghostlystudios.autolog.Models.Finals.Globals;
import com.ghostlystudios.autolog.R;
import com.ghostlystudios.autolog.Services.GeofencingServices;
import com.ghostlystudios.autolog.Views.Finals.IntentFinals;

import java.util.List;

public class KitchenListActivity extends AppCompatActivity implements ClickListener {
    String URL = "https://www.google.com/maps/dir/?api=1&dir_action=navigate&destination=";
    DatabaseAdapter databaseAdapter;
    private RecyclerView recyclerView;
    private ListView listView;
    private KitchenAdapter kitchenAdapter;
    private List<Kitchen> kitchens;
    private int routeNumber;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize objects
        //kitchenController = new KitchenController(this);
        databaseAdapter = new DatabaseAdapter(this);
        routeNumber = getIntent().getIntExtra(IntentFinals.ROUTE_NUMBER, 0);
        kitchens = new KitchenController(this).getKitchenList(routeNumber);
        kitchenAdapter = new KitchenAdapter(kitchens);
        //TODO: uncomment
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setContentView(R.layout.content_kitchen_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(layoutManager);
            //recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(kitchenAdapter);
            setRecyclerTouchListener();
        }else{
            setContentView(R.layout.kitchen_list_view);
            listView = (ListView) findViewById(R.id.list_view);
            String[] kitchenNameList = new String[kitchens.size()];
            for(int z = 0; z < kitchens.size(); z++){
                kitchenNameList[z] = kitchens.get(z).getName();
            }
            listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, kitchenNameList));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL + kitchens.get(i).getAddress())));
                }
            });
        }
        //TODO: Implement with GeofencingServices
        serviceIntent = new Intent(this, GeofencingServices.class).putExtra(Globals.ROUTENUMBER, routeNumber);
        startService(serviceIntent);
    }

    private void setRecyclerTouchListener(){
        recyclerView.addOnItemTouchListener(new ClickListener.RecyclerClickListener(this, recyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL + kitchens.
                                get(position).getAddress())));
                    }
                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    protected void onDestroy(){
        stopService(serviceIntent);
        super.onDestroy();
    }
}
