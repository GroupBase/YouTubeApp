package dev.vn.groupbase.listener;

import java.util.List;

import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 11/4/16.
 */

public interface VideoStreamListener extends ModelCallBack {
    void onLoadData(List<String> list_stream);
    void onError();
}
