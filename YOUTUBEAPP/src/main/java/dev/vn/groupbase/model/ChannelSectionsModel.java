package dev.vn.groupbase.model;

import java.util.ArrayList;

import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.database.ChannelSectionsTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.model.callback.ModelCallBackChannelSections;

/**
 * Created by acnovn on 10/19/16.
 */

public class ChannelSectionsModel extends ModelCommon {
    public static String CHENNEL_SECTIONS_TITLE = "chennel_section_title";
    public static String CHENNEL_SECTIONS_KEY = "chennel_section_key";
    public ChannelSectionsModel(ModelCallBackChannelSections callback) {
        super(callback);
    }

    @Override
    public void onStart() {
        getDataList();
    }
    private void getDataList(){
        ArrayList<ChannelSectionsTable> list = YouTubeAppManager.getChannelSectionList();
        ((ModelCallBackChannelSections) mCallBack).onLoadChannelSection(list);

    }
}
