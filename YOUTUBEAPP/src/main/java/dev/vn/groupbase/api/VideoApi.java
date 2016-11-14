package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/14/16.
 */

public class VideoApi extends OfficialApiBase {

    public VideoApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.VIDEOS, mParams);
    }

    @Override
    public void execute() {
        mParams.put("part", "snippet,status");
        this.asyncRequest(YoutubeConstant.VIDEOS, mParams);
    }
    public void setVideoId(String videoId){
        mParams.put("id",videoId);
    }
}