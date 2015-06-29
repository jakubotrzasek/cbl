package pl.jakubotrzasek.cowbehaviorlistener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jakubotrzasek on 28.06.15.
 */
public class CowData {
    private final static String TAG = "Cow_data";

    private Map<String, String> cowsBeacons = new HashMap<String, String>();
    private StorageHandler sh = new StorageHandler();
    private static String fileData = "CowData.csv";
    private static String saveFile = "CowFeedData.csv";

    public CowData() {
        String data = sh.getDataFromFile("cblData", fileData);
        String[] lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] cowId = lines[i].split(";");
            cowsBeacons.put(cowId[0], cowId[1]);
        }
    }

    public String getCowName(String beaconId) {
        return cowsBeacons.get(beaconId);
    }

    private String getCowId(String cowName) {
        for (Map.Entry<String, String> entry : cowsBeacons.entrySet()) {
            if (cowName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean saveCowData(String cowName, String value) {

        String data = new StringBuilder().
                append(DateFormat.getDateTimeInstance().format(new Date())).
                append(this.getCowId(cowName)).
                append(";").
                append(value).toString();
        sh.appendToFile("cblData", this.saveFile, data);
        return true;
    }

}
