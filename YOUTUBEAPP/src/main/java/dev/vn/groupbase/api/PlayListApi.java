package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/14/16.
 */

public class PlayListApi extends OfficialApiBase {
    private boolean isList = false;

    @Override
    public void execute() {
        mParams.put("part", "snippet,status,contentDetails");
//        if (isList) {
//            if (mParams.containsKey("id"))
//                mParams.remove("id");
//        } else {
//            if (mParams.containsKey("channelId"))
//                mParams.remove("channelId");
//        }
        this.asyncRequest(YoutubeConstant.PLAY_LIST, mParams);
    }

    public PlayListApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.PLAY_LIST, mParams);
    }

    public void isList(boolean isList) {
//        this.isList = isList;
    }

    public void setChannelId(String channelId) {
        mParams.put("channelId", channelId);
    }

    public void setIdList(String id) {
        mParams.put("id", id);
    }
}