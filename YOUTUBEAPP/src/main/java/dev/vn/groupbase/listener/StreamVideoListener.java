package dev.vn.groupbase.listener;

import gmo.hcm.net.lib.RequestError;

/**
 * Created by acnovn on 11/9/16.
 */

public interface StreamVideoListener {
    void onRequestStreamStart();
    void onRequestStreamError(RequestError error_type);
    void onRequestStreamFinish();
    void onFullScreen();
    void onExitFullScreen();
    void onLoadAdFinish();
    void onLoadAdStart();
}
