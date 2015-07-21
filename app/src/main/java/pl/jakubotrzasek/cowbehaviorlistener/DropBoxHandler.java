package pl.jakubotrzasek.cowbehaviorlistener;


import android.content.Context;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by jakubotrzasek on 20.07.15.
 */
public class DropBoxHandler {

    final static private String APP_KEY = "iwu4d3qsekold0e";
    final static private String APP_SECRET = "";
    private DropboxAPI<AndroidAuthSession> mDBApi;


    public DropBoxHandler() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }

    public boolean runDropBox(Context c) {
        mDBApi.getSession().startOAuth2Authentication(c);
        return true;
    }

    public void onResume() {

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public boolean uploadFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Entry response = null;
        try {
            response = mDBApi.putFile("/magnum-opus.txt", inputStream,
                    file.length(), null, null);
        } catch (DropboxException e) {
            Log.e("DROPBOX", e.toString());
            e.printStackTrace();
        }
        Log.e("DbExampleLog", "The uploaded file's rev is: " + response.rev);
        return true;
    }

}

