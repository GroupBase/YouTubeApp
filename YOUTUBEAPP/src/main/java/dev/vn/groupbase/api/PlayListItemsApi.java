package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/14/16.
 */

public class PlayListItemsApi extends OfficialApiBase {

    public PlayListItemsApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.PLAY_LIST_ITEMS, mParams);
    }

    @Override
    public void execute() {
        mParams.put("part", "snippet,status,contentDetails");
        this.asyncRequest(YoutubeConstant.PLAY_LIST_ITEMS, mParams);
    }
    public void setPlayListId(String id){
        mParams.put("playlistId", id);
    }
}