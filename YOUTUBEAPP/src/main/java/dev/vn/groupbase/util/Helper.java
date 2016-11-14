package dev.vn.groupbase.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by acnovn on 11/7/16.
 */

public class Helper {
    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (mConnectivityManager.getActiveNetworkInfo() != null && mConnectivityManager.getActiveNetworkInfo().isAvailable() && mConnectivityManager
                .getActiveNetworkInfo().isConnected());
    }
}
