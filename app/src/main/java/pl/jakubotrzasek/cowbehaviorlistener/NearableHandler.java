package pl.jakubotrzasek.cowbehaviorlistener;


import com.estimote.sdk.Nearable;
import com.estimote.sdk.Utils;
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
            bdt.distance = (double) n.rssi;
            bdt.jumps = n.isMoving ? 1:0;
            bdt.bpower = n.power.powerInDbm;
            bdt.name = new StringBuilder().append(n.identifier).toString();
            bdt.temp = n.temperature;
            bdt.proximity = Utils.computeProximity(n).name();
            bdt.xacc = n.xAcceleration;
            bdt.yacc = n.yAcceleration;
            bdt.zacc = n.zAcceleration;
            bdt.stateDuration = n.currentMotionStateDuration;
            ld.add(bdt);
        }
        return ld;
    }



}
