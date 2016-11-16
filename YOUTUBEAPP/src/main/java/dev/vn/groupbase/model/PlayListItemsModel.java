package dev.vn.groupbase.model;

import android.os.Bundle;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import dev.vn.groupbase.api.PlayListItemsApi;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.api.parser.PlayListItemParser;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.model.callback.ModelCallBackPlayListItems;
import gmo.hcm.net.lib.ApiListener;
import gmo.hcm.net.lib.RequestError;

import static dev.vn.groupbase.model.PlayListModel.PLAY_LIST_KEY;

/**
 * Created by acnovn on 10/25/16.
 */

public class PlayListItemsModel extends ModelCommon implements ApiListener {
    private String mPlayListID;

    @Override
    public void onStart() {
        super.onStart();
        requestPlayList();
    }


    public PlayListItemsModel (ModelCallBackPlayListItems listener) {
        super(listener);
    }

    private void requestPlayList() {
        ProgressLoading.show();
        PlayListItemsApi api = new PlayListItemsApi(this);
        api.setPlayListId(mPlayListID);
        api.execute();
    }
    public void requestPlayListMore(){
        PlayListItemsApi api = new PlayListItemsApi(new ApiListener() {
            @Override
            public void onError(RequestError requestError) {
                ((ModelCallBackPlayListItems)mCallBack).onError(RequestError.DATA_ERROR);
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                ArrayList<PlayListItemEntity> list = PlayListItemParser.parser(result.toString());
                pageInfo = PlayListItemParser.pageInfo;
                nextPageToken = PlayListItemParser.nextPageToken;
                prevPageToken = PlayListItemParser.prevPageToken;
                ((ModelCallBackPlayListItems)mCallBack).onLoadNext(list);
            }
        });
        api.setPlayListId(mPlayListID);
        api.setNextPage(this.nextPageToken);
        api.execute();
    }
    @Override
    public void onError(RequestError requestError) {
        ((ModelCallBackPlayListItems)mCallBack).onError(requestError.DATA_ERROR);
        ProgressLoading.dismiss();
    }

    @Override
    public void onFinish(Object result, boolean endRequest) {
        ArrayList<PlayListItemEntity> list = PlayListItemParser.parser(result.toString());
        this.pageInfo = PlayListItemParser.pageInfo;
        this.nextPageToken = PlayListItemParser.nextPageToken;
        this.prevPageToken = PlayListItemParser.prevPageToken;
        ((ModelCallBackPlayListItems)mCallBack).onLoadData(list);
        ProgressLoading.dismiss();

    }

    @Override
    public void getData(Bundle bundle) {
        if (bundle != null) {
            mPlayListID = bundle.getString(PLAY_LIST_KEY,"");
        }
    }
}
