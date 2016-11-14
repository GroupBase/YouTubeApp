package dev.vn.groupbase.model;

import android.os.Bundle;

import com.android.volley.VolleyError;

import dev.vn.groupbase.api.ChannelsApi;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCallBack;
import dev.vn.groupbase.common.ModelCommon;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/15/16.
 */

public class ChannelsModel extends ModelCommon implements ApiListener{
    public ChannelsModel() {
    }

   public ChannelsModel(ModelCallBack modelCallBack){
       super(modelCallBack);
   }
    @Override
    public void onStart() {
        ChannelsApi api = new ChannelsApi(this);
        api.setChannelId("UC9hjXy61UPW7iLfWswKzhww");
        api.execute();
        DebugLog.log("url",api.getUrl());
    }

    @Override
    public void onResume() {

    }

    @Override
    public void getData(Bundle bundle) {

    }

    @Override
    public void onError(VolleyError statusCode) {

    }

    @Override
    public void onFinish(Object result,boolean endRequest) {
        DebugLog.log("JsonResult:",result.toString());
        mCallBack.onBinData( result);
    }
}
