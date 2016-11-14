package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/14/16.
 */

public class ChannelsApi extends OfficialApiBase {

    public ChannelsApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.CHANNEL, mParams);
    }

    public void execute() {
        mParams.put("part", "snippet,status");
        this.asyncRequest(YoutubeConstant.CHANNEL, mParams);
    }

    public void setChannelId(String id) {
        mParams.put("id", id);
    }


}
