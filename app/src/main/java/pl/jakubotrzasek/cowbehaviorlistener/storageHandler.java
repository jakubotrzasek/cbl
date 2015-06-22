package pl.jakubotrzasek.cowbehaviorlistener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * Created by jakubotrzasek on 18.06.15.
 */
public final class storageHandler {

    private static String TAG = "Storage_FAIL";

    public storageHandler() {
        return;
    }

    public static boolean createFolder(String folderName) {
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), folderName);
            if (!newFolder.exists()) {
                newFolder.mkdir();
                Log.e(TAG, "Folder?!!!!!");
                return true;
            }
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, "Couldnt create folder", e);
            return false;
        }
        return false;

    }

    public static boolean appendToFile(String folderName, String fileName, String data) {
        File file = new File(folderName, fileName);
        if (!file.exists()) {
            try {
                Log.e("TAG", "File Create MODE!!!!");
                file.createNewFile();

            } catch (Exception e) {
                e.toString();
                Log.e("TAG", new StringBuilder("File NOT Created").append(e.toString()).toString());
                return false;
            }
        }
        try {
            FileOutputStream fosAppend = new FileOutputStream(fileName);
            fosAppend.write(data.getBytes());
            fosAppend.write(System.getProperty("line.separator").getBytes());
            fosAppend.flush();
            fosAppend.close();
            return true;
        } catch (Exception ee) {
            ee.toString();
            Log.e(TAG, "Data Save failed!!");
            return false;
        }

    }
}
