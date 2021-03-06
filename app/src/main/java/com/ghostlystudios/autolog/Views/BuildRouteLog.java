package com.ghostlystudios.autolog.Views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ghostlystudios.autolog.Controllers.DatabaseAdapter;
import com.ghostlystudios.autolog.Controllers.TempLogRequests;
import com.ghostlystudios.autolog.Controllers.VolleyRestController;
import com.ghostlystudios.autolog.Models.TempLog;
import com.ghostlystudios.autolog.R;
import com.ghostlystudios.autolog.Views.Finals.IntentFinals;

import java.util.List;

public class BuildRouteLog extends AppCompatActivity {

    //Initalize Variables
    int routeNumber;
    String userId;
    TableLayout tableLayout;
    TextView textView;
    DatabaseAdapter database;
    ProgressDialog pd;
    SharedPreferences preferences;
    List<TempLog> tempLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_rte_log);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        database = new DatabaseAdapter(this);
        //Get TempLog from database
        tempLogs = database.getTempLogs();
        tableLayout = (TableLayout) findViewById(R.id.tablebase);
        tableLayout.removeAllViews();
        //Setup Viewing Table
        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutParams);
        textView = new TextView(this);
        textView.setText("Name");
        row.addView(textView);
        textView = new TextView(this);
        textView.setText(" Time");
        row.addView(textView);
        tableLayout.addView(row, 0);
        //Fill table with TempLog
        for(TempLog log : tempLogs) {
            row = new TableRow(this);
            row.setLayoutParams(layoutParams);
            textView = new TextView(this);
            textView.setText(log.getName());
            row.addView(textView);
            textView = new TextView(this);
            textView.setText(" " + log.getDate());
            row.addView(textView);
            tableLayout.addView(row);
        }
        //use Toast to notify complete upload
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        AlertDialog dialog = buildDialog();
        dialog.show();
        //TODO: Setup Rest API to accept JSON below
        //uploadTempLogs();
        return super.onOptionsItemSelected(i);
    }
    private void uploadTempLogs(){
        VolleyRestController.getInstance(this).addToRequestQueue(new TempLogRequests(this).setLogs(database.getTempLogsMap()));
    }

    private AlertDialog buildDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure?").setTitle("Confirmation?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveDelete();
            }
        });
        return builder.create();
    }

    private void positiveDelete(){
        new DatabaseAdapter(this).resetLogs();
        this.recreate();
    }
}