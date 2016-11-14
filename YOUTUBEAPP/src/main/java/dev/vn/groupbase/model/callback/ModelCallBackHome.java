package dev.vn.groupbase.model.callback;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 10/26/16.
 */

public interface ModelCallBackHome extends ModelCallBack {
    void onLoadNew(ArrayList<PlayListItemEntity> list);
}
