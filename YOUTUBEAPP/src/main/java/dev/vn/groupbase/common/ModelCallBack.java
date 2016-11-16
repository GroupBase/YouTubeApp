package dev.vn.groupbase.common;

import java.util.Objects;

import gmo.hcm.net.lib.RequestError;

/**
 * Created by acnovn on 10/15/16.
 */

public interface ModelCallBack {
    void onError (RequestError error_type);
}
