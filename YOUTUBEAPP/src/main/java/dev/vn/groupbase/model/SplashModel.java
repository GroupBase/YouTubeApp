package dev.vn.groupbase.model;

import android.os.Handler;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import app.thn.groupbase.phimkinhdi.R;
import dev.vn.groupbase.api.ChannelSectionsApi;
import dev.vn.groupbase.api.entity.ChannelSectionEntity;
import dev.vn.groupbase.api.parser.ChannelSectionParser;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.model.callback.ModelCallBackSplash;
import gmo.hcm.net.lib.ApiListener;
import gmo.hcm.net.lib.RequestError;


/**
 * Created by acnovn on 11/7/16.
 */

public class SplashModel extends ModelCommon {
    private Handler handler = new Handler();
    public SplashModel(ModelCallBackSplash listener) {
        super(listener);
    }

    @Override
    public void onStart() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestChannelSection();
            }
        },2000);

    }

    public void requestChannelSection() {

        ChannelSectionsApi api = new ChannelSectionsApi(new ApiListener() {
            @Override
            public void onError(RequestError requestError) {
                if (requestError == RequestError.NETWORK){
                    ((ModelCallBackSplash)mCallBack).onError(RequestError.NETWORK);
                    ((ModelCallBackSplash)mCallBack).complete(false);
                } else {
                    DebugLog.showToast("network error");
                    ((ModelCallBackSplash)mCallBack).onError(RequestError.NETWORK_LOST);
                    ((ModelCallBackSplash)mCallBack).complete(false);
                }
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                ArrayList<ChannelSectionEntity> lst= ChannelSectionParser.parser(result.toString());
                //ok insert db
                if (lst.size()>0) {
                    ((ModelCallBackSplash)mCallBack).complete(YouTubeAppManager.insertChannelSectionList(lst));
                } else {
                    ((ModelCallBackSplash)mCallBack).complete(false);
                }

            }
        });
        api.setChannelId(mContext.getString(R.string.channel_key));
        api.execute();
    }
}
