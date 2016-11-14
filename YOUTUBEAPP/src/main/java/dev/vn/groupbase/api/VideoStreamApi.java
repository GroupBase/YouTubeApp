package dev.vn.groupbase.api;

import java.util.HashMap;
import java.util.Map;

import dev.vn.groupbase.App;
import dev.vn.groupbase.DeveloperKey;
import gmo.hcm.net.lib.ApiBase;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 11/4/16.
 */

public class VideoStreamApi extends ApiBase {
    protected Map<String, String> mParams = new HashMap<>();
    private static String mUrl = "https://www.youtube.com/get_video_info";
    public VideoStreamApi(ApiListener apiListener) {
        super(apiListener);
    }
    public void execute(){
        mParams.put("el","embedded");
        mParams.put("ps","default");
        mParams.put("eurl","");
        mParams.put("gl","US");
        mParams.put("hl","en");
        HashMap<String,String>header = new HashMap<>();
        header.put("User-Agent", App.getBrowserUserAgent());
        this.setHeader(header);
        this.asyncRequest(mUrl,mParams);
    }
    public void setVideoId(String videoId){
        mParams.put("video_id",videoId);
    }
    public String getUrl() {
        return makeUrl(mUrl, mParams);
    }
}
