package dev.vn.groupbase.api;

import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/14/16.
 */

public class SearchApi extends OfficialApiBase {

    public SearchApi(ApiListener apiListener) {
        super(apiListener);
    }

    @Override
    public String getUrl() {
        return makeUrl(YoutubeConstant.SEARCH, mParams);
    }

    @Override
    public void execute() {
        mParams.put("part", "snippet");
        mParams.put("type", "video");
        mParams.put("videoType","movie");
        this.asyncRequest(YoutubeConstant.SEARCH, mParams);
    }
    public void setkeyWord(String keyword){
        mParams.put("q", keyword);
    }
}