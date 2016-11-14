package dev.vn.groupbase.model;

import android.os.Bundle;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import dev.vn.groupbase.api.PlayListApi;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.api.parser.PlayListParser;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.listener.PlayListListener;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListModel extends ModelCommon {
    public static String PLAY_LIST_KEY = "play_list_id";
    public static String LIST_PLAY_LIST_KEY = "list_play_list_id";
    public static String LIST_PLAY_TITLE = "play_list_title";
    public static String LIST_PLAY_IMAGE = "play_list_image";
    private String mListPlayListId;
    private String mTitleHeader = "";
    public PlayListModel (PlayListListener listener) {
        super(listener);
    }
    @Override
    public void onStart() {
        requestPlayList();
    }

    private void requestPlayList() {
        String listId= "";
        listId = mListPlayListId.replace("[","").replace("]","");
        ProgressLoading.show();
        PlayListApi api = new PlayListApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {
                ProgressLoading.dismiss();
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                DebugLog.log("JsonResult:",result.toString());
                ArrayList<PlayListEntity>lst=PlayListParser.parser(result.toString());
                ((PlayListListener)mCallBack).onLoadData(lst);
                ProgressLoading.dismiss();
            }
        });
        api.isList(true);
        api.setIdList(listId.toString());
        api.setMaxResults("50");
        api.execute();
    }

    @Override
    public void getData(Bundle bundle) {
        if(bundle.containsKey(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE)) {
            mTitleHeader = bundle.getString(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE);
        }
        if (bundle.containsKey(LIST_PLAY_LIST_KEY)){
            mListPlayListId = bundle.getString(LIST_PLAY_LIST_KEY);
        }
    }
}
