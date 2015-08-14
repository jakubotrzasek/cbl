package pl.jakubotrzasek.cowbehaviorlistener;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by jakubotrzasek on 22.07.15.
 */
public class BackGroundService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private DropBoxHandler dph;
    private BeaconManager beaconManager;
    private Intent i;
    private StorageHandler sh;
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private String scanId = "";
    private String TAG = "Service";
    public BackGroundService(String name) {
        super(name);
    }

    public BackGroundService() {
        super("empty");
        return;
    }

    public void textAdd(String t) {
        Intent localIntent = new Intent("beaconsFound");
        localIntent.putExtra("serviceData", t);
        sendBroadcast(localIntent);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        if (beaconManager == null) {
            beaconManager = new BeaconManager(this);
        }
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

        String phoneName = workIntent.getStringExtra("phoneName");
        dph = new DropBoxHandler(phoneName);
        dph.runDropBox(workIntent.getStringExtra("dphToken"));
        sh = new StorageHandler(dph);
        i = new Intent(getApplicationContext(), FeedActivity.class);
        if (beaconManager == null) {
            beaconManager = new BeaconManager(this);
        }

        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> nearables) {
                NearableHandler nh = new NearableHandler(nearables);
                List<BeaconData> bdatas = nh.getBeaconsDistance();
                if (bdatas != null && bdatas.toArray().length > 0) {
                    int a = 0;
                    String beaconData = "";
                    for (BeaconData b : bdatas) {
                        beaconData += new StringBuilder().append(":").append(b.toString()).toString();
                        String format = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
                        sh.createFolder("cblData");
                        sh.appendToFile("cblData", new StringBuilder().append(sdf.format(new Date())).append(";").append(b.toCSV()).toString());
                        if (a < 3) {
                            i.putExtra("cow" + a, b.name);
                            a++;
                        }

                    }
                    textAdd(beaconData);
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        try {

            beaconManager.stopNearableDiscovery(scanId); // .stopBeaconDiscovery(scanId);
            // beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (Exception e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

}
