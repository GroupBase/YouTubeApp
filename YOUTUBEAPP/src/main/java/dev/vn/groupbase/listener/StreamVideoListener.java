package dev.vn.groupbase.listener;

/**
 * Created by acnovn on 11/9/16.
 */

public interface StreamVideoListener {
    void onRequestStreamStart();
    void onRequestStreamError();
    void onRequestStreamFinish();
    void onFullScreen();
    void onExitFullScreen();
    void onLoadAdFinish();
    void onLoadAdStart();
}
