package pl.jakubotrzasek.cowbehaviorlistener;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jakubotrzasek on 18.06.15.
 */
public final class StorageHandler {
    private static String TAG = "Storage_FAIL";
    private static String placeHolder = "/storage/sdcard1/";
    private static DropBoxHandler dropbox;
    private boolean saveToDPFlag = true;

    public StorageHandler(DropBoxHandler dph) {
        dropbox = dph;
    }

    public StorageHandler() {
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

    public boolean appendToFile(String folderName, String data) {
        String format = "yyyy-MM-dd HH";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        SimpleDateFormat minutes = new SimpleDateFormat("m", Locale.UK);
        int i_minutes = Integer.parseInt(minutes.format(new Date()));

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
            if ((i_minutes % 10 == 0 || i_minutes == 0) && saveToDPFlag == true && dropbox != null) {

                try {
                    Log.e("TAG", "trying to send to dp");
                    saveToDPFlag = false;
                    new UploadTask().execute(placeHolder + folderName + "/" + fileName);

                } catch (Exception eee) {
                    eee.toString();
                    Log.e("TAG", "dp failed:" + eee.toString());
                }
            }
            if (i_minutes % 10 != 0 && i_minutes != 0) {
                saveToDPFlag = true;
            }
            return true;
        } catch (Exception ee) {
            ee.toString();
            Log.e(TAG, ee.toString());
            return false;
        }


    }

    private class UploadTask extends AsyncTask {
        protected void onProgressUpdate(Integer... progress) {
            Log.i("ASYNC", "async progress");
        }

        protected void onPostExecute(Long result) {
            Log.i("ASYNC", "async ok");

        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                saveToDPFlag = false;
                dropbox.uploadFile(params[0].toString());
                Log.e("TAG", "sent to dp");
            } catch (Exception e) {

                Log.e("TAG", e.toString());
                return null;
            }
            return null;
        }
    }

    public String getDataFromFile(String folderName, String fileName) {
        String output = "";
        try {
            BufferedReader buf = new BufferedReader(new FileReader(this.placeHolder + folderName + "/" + fileName));
            for (String line = buf.readLine(); line != null; line = buf.readLine()) {
                output += line + "\n";
            }
        } catch (Exception e) {
            e.toString();
            Log.e(TAG, e.toString());
            return "false";
        }

        return output;
    }
}
