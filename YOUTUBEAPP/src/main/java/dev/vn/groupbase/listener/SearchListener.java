package dev.vn.groupbase.listener;

import java.util.List;

import dev.vn.groupbase.api.entity.SearchEnity;
import dev.vn.groupbase.common.ModelCallBack;

/**
 * Created by acnovn on 10/27/16.
 */

public interface SearchListener extends ModelCallBack {
    void onLoadData(List<SearchEnity> list);
}
