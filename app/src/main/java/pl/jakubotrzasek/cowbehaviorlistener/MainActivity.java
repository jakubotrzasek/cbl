package pl.jakubotrzasek.cowbehaviorlistener;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private static final String TAG = "MyActivity";

   // protected Context context;
    private TextView t;
    private String scanId ="";
    private BeaconManager beaconManager; // = new BeaconManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=new TextView(this);
        beaconManager =new BeaconManager(this);
        t=(TextView)findViewById(R.id.textView01);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                BeaconHandler bh = new BeaconHandler(beacons);
                List<BeaconData> bdatas =  bh.getBeaconsDistance();
                for(BeaconData b : bdatas ) {
                    t.append(new StringBuilder().append(":").append(b.toString()));
                }
                Log.d(TAG, "Ranged beacons: " + beacons);
                           }
        });

        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override public void onNearablesDiscovered(List<Nearable> nearables) {
                NearableHandler nh = new NearableHandler(nearables);
                List<BeaconData> bdatas =  nh.getBeaconsDistance();
                t.setText("");

                if (bdatas != null && bdatas.toArray().length >0) {
                    for (BeaconData b : bdatas) {
                       t.append(new StringBuilder().append(":").append(b.toString()));
                        storageHandler sh = new storageHandler();
                        sh.createFolder("cblData");
                        sh.appendToFile("cblData", "cbl_nearableData.csv", b.toCSV());
                    }
                }
                Log.d(TAG, "Discovered nearables: " + nearables);
                
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                 //   beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                        @Override public void onServiceReady() {
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
    protected void onStop () {
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
