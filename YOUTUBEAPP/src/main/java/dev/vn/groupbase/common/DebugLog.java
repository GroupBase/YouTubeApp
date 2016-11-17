package dev.vn.groupbase.common;

import android.widget.Toast;

import app.thn.groupbase.gameshow.BuildConfig;


/**
 * Created by acnovn on 10/16/16.
 */

public class DebugLog {
    public static void log(String tag, String sms) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i("App_"+tag, sms);
        }
    }
    public static void log_e(String tag, String sms) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e("Error_"+tag, sms);
        }
    }
    public static void showToast(String sms) {
        if (BuildConfig.DEBUG) {
            android.widget.Toast.makeText(ViewManager.getInstance().getActivity(),sms, Toast.LENGTH_LONG).show();
        }
    }
}
