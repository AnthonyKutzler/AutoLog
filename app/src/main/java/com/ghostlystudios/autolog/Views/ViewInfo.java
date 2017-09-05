/*package com.ghostlystudios.autolog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ghostlystudios.qcroutelog.Models.MySQHelp;
import com.ghostlystudios.qcroutelog.R;
import com.ghostlystudios.qcroutelog.Models.StopInter;

public class ViewInfo extends AppCompatActivity implements View.OnTouchListener {
    TextView[] a = new TextView[5];
    String[] c = new String[5];
    String rteid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);
        int[] z = {R.id.rname, R.id.retime, R.id.rltime, R.id.raddress, R.id.rnote};
        Intent intent = getIntent();
        rteid = intent.getExtras().getString("rten");
        for (int x = 0; x < a.length; x++) {
            a[x] = (TextView) findViewById(z[x]);
        }
        MySQHelp db = new MySQHelp(this);
        StopInter m = db.getStop(rteid);
        c[0] = m.getName();
        c[1] = m.getEtime();
        c[2] = m.getLtime();
        c[3] = m.getAddress() + " " + m.getCity() + ", " + m.getState();
        //c[4] = m.getPhone();
        c[4] = m.getNote();
        for(int y = 0; y < a.length; y++) {
            a[y].setText(c[y]);
        }
        a[3].setOnTouchListener(this);
        //a[4].setOnTouchListener(this);
    }
        @Override
        public boolean onTouch(View f, MotionEvent me) {
            //switch (f.getId()) {
                //case R.id.raddress:
                    Uri mapuri = Uri.parse("google.navigation:q=" + c[3]);
                    Intent mi = new Intent(Intent.ACTION_VIEW, mapuri);
                    mi.setPackage("com.google.android.apps.maps");
                    if (mi.resolveActivity(getPackageManager()) != null)
                        startActivity(mi);
                    /*break;
                case R.id.rphone:
                    Intent mc = new Intent(Intent.ACTION_CALL, Uri.parse(c[4]));
                    try {
                        startActivity(mc);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Cannot find Activity", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }///first end-------------------------------------
            return false;
        }
}*/