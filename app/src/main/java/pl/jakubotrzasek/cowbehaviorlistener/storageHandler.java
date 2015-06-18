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
public class storageHandler {

    String TAG = "storage Handler";

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getStorageDir(String newFileLog) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), newFileLog);
        if (!file.mkdirs()) {
            // fail here in a epic way.
            Log.e(TAG, "failed to make your dir");
        }
        return file;
    }

    public boolean writeToFile(String fileName, String data) {
        if (isExternalStorageWritable()) {
            try {
                FileWriter writer = new FileWriter(this.getStorageDir("test"));
                writer.write(data);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                return false;
            }
            return true;
        } else
        {
            return false;
        }
    }

}
