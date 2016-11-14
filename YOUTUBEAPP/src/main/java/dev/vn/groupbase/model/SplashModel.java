package dev.vn.groupbase.model;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.api.ChannelSectionsApi;
import dev.vn.groupbase.api.entity.ChannelSectionEntity;
import dev.vn.groupbase.api.parser.ChannelSectionParser;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.model.callback.ModelCallBackSplash;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 11/7/16.
 */

public class SplashModel extends ModelCommon {
    public SplashModel(ModelCallBackSplash listener) {
        super(listener);
    }

    @Override
    public void onStart() {
        requestChannelSection();
    }

    public void requestChannelSection() {

        ChannelSectionsApi api = new ChannelSectionsApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {
                ((ModelCallBackSplash)mCallBack).complete(false);
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
