package pl.jakubotrzasek.cowbehaviorlistener;


import com.estimote.sdk.Nearable;
import com.estimote.sdk.Utils;
import com.estimote.sdk.cloud.model.BroadcastingPower;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by jakubotrzasek on 18.06.15.
 */
public class NearableHandler {

    private List<Nearable> nearables;
    private List<BeaconData> beaconsDistance;

    public NearableHandler(List<Nearable> nearables_) {
        nearables = nearables_;
    }

    public List<BeaconData> getBeaconsDistance() {
        if (nearables == null || nearables.toArray().length == 0 ) {
            return null;
        }
        List<BeaconData> ld= new ArrayList<BeaconData>();
        for(Nearable n : nearables) {
            BeaconData bdt = new BeaconData();
            bdt.distance =   (double) n.power.powerInDbm;
            bdt.jumps = n.isMoving ? 1:0;
           // bdt.distance =  Math.min(Utils. , 6.0);
            bdt.name = n.identifier;
            bdt.temp = n.temperature;
            bdt.proximity = Utils.computeProximity(n).name();
            ld.add(bdt);
        }
        return ld;
    }



}
