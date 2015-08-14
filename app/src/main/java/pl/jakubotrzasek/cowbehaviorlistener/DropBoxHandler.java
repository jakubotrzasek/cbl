package pl.jakubotrzasek.cowbehaviorlistener;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.estimote.sdk.cloud.internal.ApiUtils.getSharedPreferences;

/**
 * Created by jakubotrzasek on 20.07.15.
 */
public class DropBoxHandler {

    final static private String APP_KEY = "iwu4d3qsekold0e";
    final static private String APP_SECRET = "iwu4d3qsekold0e";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private String dpDir = "/cbl/";
    private Context context;
    private static String dpAccessToken = "dpAccessToken";
    private static String gToken = null;

    public DropBoxHandler(String mobileName) {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        if (mobileName != null) {
            dpDir = dpDir + mobileName + "/";
        }
    }

    public boolean runDropBox(Context c) {
        context = c;

        SharedPreferences sp = getSharedPreferences(context);
        gToken = sp.getString(dpAccessToken, null);
        if (gToken != null) {
            mDBApi.getSession().setOAuth2AccessToken(gToken);
            return true;
        } else {
            mDBApi.getSession().startOAuth2Authentication(c);
            return true;
        }
    }

    public String getSessionToken() {
        // gToken = mDBApi.getSession().
        return gToken;
    }


    public String getToken() {
        if (gToken != null) {
            return gToken;
        }

        if (context != null) {
            SharedPreferences sp = getSharedPreferences(context);
            return sp.getString(dpAccessToken, null);
        }
        return null;
    }

    public boolean runDropBox(String token) {

        if (token != null) {
            mDBApi.getSession().setOAuth2AccessToken(token);
        }
        return true;
    }



    public void onResume() {

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                SharedPreferences sp = getSharedPreferences(context);
                sp.edit().putString(dpAccessToken, accessToken).apply();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public boolean uploadFile(String path, String fileName) throws FileNotFoundException {
        File file = new File(path + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Entry existingEntry;
        String rev = "";
        try {
            existingEntry = mDBApi.metadata(dpDir + fileName, 1, null, false, null);
            rev = existingEntry.rev;
        } catch (Exception e) {
            Log.e("DbExampleLog", "file not found:" + dpDir + fileName + e.toString());
        }

        Entry response = null;
        try {
            if (rev.equals("")) {
                rev = null;
            }
            response = mDBApi.putFile(dpDir + fileName, inputStream,
                    file.length(), rev, null);
        } catch (DropboxException e) {
            Log.e("DROPBOX_filePUT", e.toString());
            e.printStackTrace();
        }
        return true;
    }

}

