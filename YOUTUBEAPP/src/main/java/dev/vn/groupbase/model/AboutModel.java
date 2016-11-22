package dev.vn.groupbase.model;

import java.util.List;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.api.CheckAppApi;
import dev.vn.groupbase.api.entity.YouTubeEntity;
import dev.vn.groupbase.api.parser.YouTubeParse;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.model.callback.ModelCallBackAbout;
import gmo.hcm.net.lib.ApiListener;
import gmo.hcm.net.lib.RequestError;

/**
 * Created by nghiath on 11/18/16.
 */

public class AboutModel extends ModelCommon {
    public AboutModel(ModelCallBackAbout callback){
        super(callback);
    }
    public void checkApp(){
        ProgressLoading.show();
        CheckAppApi api = new CheckAppApi(new ApiListener() {
            @Override
            public void onError(RequestError requestError) {
                if (requestError == RequestError.NETWORK){
                    DebugLog.showToast("network error");
                    ((ModelCallBackAbout)mCallBack).onError(RequestError.NETWORK);
                } else {
                    DebugLog.showToast("network error");
                    ((ModelCallBackAbout)mCallBack).onError(RequestError.NETWORK_LOST);
                }
                ProgressLoading.dismiss();
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                ProgressLoading.dismiss();
                List<YouTubeEntity> list = YouTubeParse.parser(result.toString());
                if (list.size()>0) {
                    for (YouTubeEntity obj : list) {
                        if (obj.packageId.equalsIgnoreCase(mContext.getString(R.string.packageId))) {
                            ((ModelCallBackAbout) mCallBack).onData(obj);
                            break;
                        }
                    }
                } else {
                    YouTubeEntity obj = new YouTubeEntity();
                    obj.status = 2;
                    ((ModelCallBackAbout) mCallBack).onData(obj);
                }
            }
        });
        api.execute();
    }
}
