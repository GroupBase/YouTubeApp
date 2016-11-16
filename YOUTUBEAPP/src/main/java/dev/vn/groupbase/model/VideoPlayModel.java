package dev.vn.groupbase.model;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import dev.vn.groupbase.api.VideoApi;
import dev.vn.groupbase.api.entity.VideoEntity;
import dev.vn.groupbase.api.parser.VideoParser;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.model.callback.ModelCallBackVideoPlay;
import gmo.hcm.net.lib.ApiListener;
import gmo.hcm.net.lib.NetworkUtil;
import gmo.hcm.net.lib.RequestError;

/**
 * Created by nghiath on 11/10/16.
 */

public class VideoPlayModel extends ModelCommon {
    public VideoPlayModel(ModelCallBackVideoPlay listener){
        super(listener);
    }
    public void requestVideo(String videoId){
        VideoApi api = new VideoApi(new ApiListener() {
            @Override
            public void onError(RequestError requestError) {
                if (!NetworkUtil.isNetworkConnected(mContext)) {
                    ((ModelCallBackVideoPlay) mCallBack).onError(RequestError.NETWORK);
                }
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                ArrayList<VideoEntity>list = VideoParser.parser(result.toString());
                if (list.size()>0){
                    ((ModelCallBackVideoPlay)mCallBack).onData(list.get(0));
                }else {
                    ((ModelCallBackVideoPlay)mCallBack).onData(null);
                }

            }
        });
        api.setVideoId(videoId);
        api.execute();
    }
}
