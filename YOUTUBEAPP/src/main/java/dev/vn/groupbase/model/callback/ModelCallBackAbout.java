package dev.vn.groupbase.model.callback;

import dev.vn.groupbase.api.entity.YouTubeEntity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 11/9/16.
 */

public interface ModelCallBackAbout extends ModelCallBack {
    void onData(YouTubeEntity data);
}
