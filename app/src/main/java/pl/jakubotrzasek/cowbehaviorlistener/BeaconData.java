package pl.jakubotrzasek.cowbehaviorlistener;

/**
 * Created by jakubotrzasek on 23.05.15.
 */
public class BeaconData {
    public Double distance = 0.0;
    public String name = "";
    public double temp = 0.0;
    public int jumps = 0;
    public String proximity = "";
    public double xacc = 0.0;
    public double yacc = 0.0;
    public double zacc = 0.0;
    public int bpower = 0;
    public long stateDuration;
    public String bateryLevel = "";

    public String toString() {
        return new StringBuilder().append("name:").append(this.name).
                append(",distance:").append(this.distance).
                append(",jumps:").append(this.jumps).
                append(",temp:").append(this.temp).
                append(",proximity:").append(this.proximity).
                append(",x_acceleration:").append(this.xacc).
                append(",y_acceleration:").append(this.yacc).
                append(",z_acceleration:").append(this.zacc).
                append(",state_duration:").append(this.stateDuration).
                append(",broadc.power:").append(this.bpower).
                append("batery_level:").append(this.bateryLevel).
                toString();
    }

    public String toCSV() {

        return new StringBuilder().append(this.name).append(";").
                append(this.distance).append(";").
                append(this.jumps).append(";").
                append(this.temp).append(";").
                append(this.proximity).append(";").
                append(this.xacc).append(";").
                append(this.yacc).append(";").
                append(this.zacc).append(";").
                append(this.stateDuration).append(";").
                append(this.bpower).append(";").
                append(this.bateryLevel).append(";").
                toString();
    }

}
