package dev.vn.groupbase.model.callback;

import dev.vn.groupbase.api.entity.VideoEntity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 11/9/16.
 */

public interface ModelCallBackVideoPlay extends ModelCallBack {
    void onData(VideoEntity data);
}
