package dev.vn.groupbase.model.callback;

import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 11/9/16.
 */

public interface ModelCallBackStreamVideo extends ModelCallBack {
    void onData(String url);
}
