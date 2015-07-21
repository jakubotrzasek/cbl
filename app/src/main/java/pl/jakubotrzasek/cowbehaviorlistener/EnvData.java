package pl.jakubotrzasek.cowbehaviorlistener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jakubotrzasek on 21.07.15.
 */
public class EnvData {
    private StorageHandler sh = new StorageHandler();
    private static String fileData = "EnvData.csv";
    private Map<String, String> settingsData = new HashMap<String, String>();


    public EnvData() {
        String data = sh.getDataFromFile("cblData", fileData);
        String[] lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] cowId = lines[i].split("=");
            settingsData.put(cowId[0], cowId[1]);
        }

    }

    public String getSettingVal(String propertyName) {
        return settingsData.get(propertyName);
    }
}
