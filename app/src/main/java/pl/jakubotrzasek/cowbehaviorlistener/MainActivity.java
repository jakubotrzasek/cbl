package pl.jakubotrzasek.cowbehaviorlistener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private static final String TAG = "MyActivity";
    private TextView t;
    private String scanId = "";
    private BeaconManager beaconManager;
    private Intent i;
    private StorageHandler sh;
    private DropBoxHandler dph;
    private EnvData envData = new EnvData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dph = new DropBoxHandler(envData.getSettingVal("mname"));
        dph.runDropBox(this);
        sh = new StorageHandler(dph);
        i = new Intent(getApplicationContext(), FeedActivity.class);
        t = new TextView(this);
        if (beaconManager == null) {
            beaconManager = new BeaconManager(this);
        }
        t = (TextView) findViewById(R.id.textView02);
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> nearables) {
                NearableHandler nh = new NearableHandler(nearables);
                List<BeaconData> bdatas = nh.getBeaconsDistance();
                t.setText("");
                if (bdatas != null && bdatas.toArray().length > 0) {
                    int a = 0;
                    for (BeaconData b : bdatas) {
                        t.append(new StringBuilder().append(":").append(b.toString()));

                        sh.createFolder("cblData");
                        sh.appendToFile("cblData", new StringBuilder().append(DateFormat.getDateTimeInstance().format(new Date())).append(";").append(b.toCSV()).toString());

                        if (a < 3) {
                            i.putExtra("cow" + a, b.name);
                            a++;
                        }

                    }

                }
                Log.d(TAG, "Discovered nearables: " + nearables);

            }
        });
        buttonsActions();
    }


    private void buttonsActions() {
        try {
            Button button;
            button = (Button) findViewById(R.id.feedButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(i);
                }
            });
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dph.onResume();

    }


    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    //   beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                        @Override
                        public void onServiceReady() {
                            scanId = beaconManager.startNearableDiscovery();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            beaconManager.stopNearableDiscovery(scanId); // .stopBeaconDiscovery(scanId);
            // beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (Exception e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
