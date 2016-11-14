package dev.vn.groupbase.listener;

import java.util.List;

import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 10/27/16.
 */

public interface PlayListItemsListener extends ModelCallBack {
    void onLoadData(List<PlayListItemEntity> list);
}
