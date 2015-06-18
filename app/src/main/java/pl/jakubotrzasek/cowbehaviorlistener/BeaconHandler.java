package pl.jakubotrzasek.cowbehaviorlistener;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.List;
import com.estimote.sdk.Utils;


/**
 * Created by jakubotrzasek on 19.05.15.
 */
public class BeaconHandler {

    private List<Beacon> beacons;
    private List<BeaconData> beaconsDistance;

    public BeaconHandler(List<Beacon> beacons_) {
        beacons = beacons_;
    }

    public List<BeaconData> getBeaconsDistance() {
        if (beacons == null || beacons.toArray().length == 0 ) {
            return null;
        }
        List<BeaconData> ld= new ArrayList<BeaconData>();
        for(Beacon b : beacons) {
            BeaconData bdt = new BeaconData();
            bdt.distance =  Math.min(Utils.computeAccuracy(b), 6.0);
            bdt.name = b.getName();

            ld.add(bdt);
        }
        return ld;
    }



}
