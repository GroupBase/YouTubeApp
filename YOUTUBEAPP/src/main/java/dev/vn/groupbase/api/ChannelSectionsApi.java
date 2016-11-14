package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/19/16.
 */

public class ChannelSectionsApi extends OfficialApiBase {
    public ChannelSectionsApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.CHANNEL_SECTION, mParams);
    }

    public void execute() {
        mParams.put("part", "contentDetails,snippet");
        this.asyncRequest(YoutubeConstant.CHANNEL_SECTION, mParams);
    }
    public void setChannelId(String channelId) {
        mParams.put("channelId", channelId);
    }
}
