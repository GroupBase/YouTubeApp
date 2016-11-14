package dev.vn.groupbase.model.callback;

import java.util.ArrayList;

import dev.vn.groupbase.common.ModelCallBack;
import dev.vn.groupbase.database.ChannelSectionsTable;

/**
 * Created by acnovn on 10/27/16.
 */

public interface ModelCallBackChannelSections extends ModelCallBack {
    void onLoadChannelSection(ArrayList<ChannelSectionsTable>lst);
}
