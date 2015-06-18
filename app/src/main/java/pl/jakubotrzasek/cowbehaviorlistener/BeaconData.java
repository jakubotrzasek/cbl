package pl.jakubotrzasek.cowbehaviorlistener;

/**
 * Created by jakubotrzasek on 23.05.15.
 */
public class BeaconData {
    public Double distance=0.0;
    public String name = "";
    public double temp = 0.0;
    public int jumps = 0;
    public String proximity = "";

    public String toString() {
        return new StringBuilder().append("name:").append(this.name).
                append(",distance:").append(this.distance).
                append(",jumps:").append(this.jumps).
                append(",temp:").append(this.temp).
                append(",proximity:").append(this.proximity).
                toString();
    }

}
