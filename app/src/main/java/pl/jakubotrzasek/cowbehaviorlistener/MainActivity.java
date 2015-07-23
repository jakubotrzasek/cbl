package pl.jakubotrzasek.cowbehaviorlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MyActivity";
    private TextView t;
    private String scanId = "";
    // private BeaconManager beaconManager;
    private Intent i;
    private StorageHandler sh;
    private DropBoxHandler dph;
    private EnvData envData = new EnvData();

    private Intent mServiceIntent;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dph = new DropBoxHandler(envData.getSettingVal("mname"));
        dph.runDropBox(this);
        i = new Intent(getApplicationContext(), FeedActivity.class);
        registerBroadcastReceivers();
        buttonsActions();
    }

    private Context getActivity() {
        return this;
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
        //dph.onResume();
        mServiceIntent = new Intent(getActivity(), BackGroundService.class);
        mServiceIntent.putExtra("phoneName", envData.getSettingVal("mname"));
        mServiceIntent.putExtra("dphToken", dph.getToken());
        getActivity().startService(mServiceIntent);
        registerBroadcastReceivers();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastReceiver);
            // beaconManager.stopNearableDiscovery(scanId); // .stopBeaconDiscovery(scanId);
            // beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (Exception e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // beaconManager.disconnect();
    }


    private void registerBroadcastReceivers() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String arg1 = intent.getStringExtra("serviceData");
                t = (TextView) findViewById(R.id.textView02);
                t.setText(arg1);

            }
        };
        IntentFilter progressFilter = new IntentFilter("beaconsFound");
        registerReceiver(broadcastReceiver, progressFilter);
    }


}
