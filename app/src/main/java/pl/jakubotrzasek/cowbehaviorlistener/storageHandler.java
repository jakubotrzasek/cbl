package pl.jakubotrzasek.cowbehaviorlistener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jakubotrzasek on 18.06.15.
 */
public final class storageHandler {

    private static String TAG = "Storage_FAIL";
    private static String placeHolder = "/storage/sdcard1/";

    public storageHandler() {
        return;
    }

    public static boolean createFolder(String folderName) {
        try {
            File newFolder = new File(placeHolder + folderName);
            newFolder.mkdirs();
                return true;
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, "Couldn't create folder", e);
            return false;
        }

    }

    public static boolean appendToFile(String folderName, String fileName, String data) {
        File file = new File(placeHolder + folderName, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (Exception e) {
                e.toString();
                Log.e("TAG", new StringBuilder("File NOT Created").append(e.toString()).toString());
                return false;
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(placeHolder + folderName + "/" + fileName, true));
            buf.append(data);
            buf.newLine();
            buf.close();
            return true;
        } catch (Exception ee) {
            ee.toString();
            Log.e(TAG, ee.toString());
            return false;
        }

    }

    public static boolean appendToFile(String folderName, String data) {
        String format = "yyyy-MM-dd HH";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        String fileName = sdf.format(new Date()) + ".csv";
        File file = new File(placeHolder + folderName, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (Exception e) {
                e.toString();
                Log.e("TAG", new StringBuilder("File NOT Created").append(e.toString()).toString());
                return false;
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(placeHolder + folderName + "/" + fileName, true));
            buf.append(data);
            buf.newLine();
            buf.close();
            return true;
        } catch (Exception ee) {
            ee.toString();
            Log.e(TAG, ee.toString());
            return false;
        }

    }
}
