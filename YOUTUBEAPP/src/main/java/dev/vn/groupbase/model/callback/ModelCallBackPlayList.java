package dev.vn.groupbase.model.callback;

import java.util.List;

import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 10/27/16.
 */

public interface ModelCallBackPlayList extends ModelCallBack {
    void onLoadData(List<PlayListEntity> list);
}
