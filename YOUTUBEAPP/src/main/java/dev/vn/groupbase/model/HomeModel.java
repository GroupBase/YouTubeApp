package dev.vn.groupbase.model;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Random;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.api.ChannelSectionsApi;
import dev.vn.groupbase.api.PlayListItemsApi;
import dev.vn.groupbase.api.entity.ChannelSectionEntity;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.api.parser.ChannelSectionParser;
import dev.vn.groupbase.api.parser.PlayListItemParser;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.model.callback.ModelCallBackHome;
import dev.vn.groupbase.util.Helper;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/25/16.
 */

public class HomeModel extends ModelCommon {
    private static String TAG = HomeModel.class.getSimpleName();

    public HomeModel(ModelCallBackHome listener) {
        super(listener);
    }

    @Override
    public void onStart() {
        requestChannelSection();
    }

    public void requestChannelSection() {
        if (!Helper.isNetworkConnected(mContext)){
            ((ModelCallBackHome) mCallBack).onError(ERROR_TYPE.NETWORK);
            return;
        }
        ChannelSectionsApi api = new ChannelSectionsApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {
                DebugLog.log_e(TAG, "requestChannelSection");
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                DebugLog.log(TAG, "requestChannelSection");
                ArrayList<ChannelSectionEntity> list = ChannelSectionParser.parser(result.toString());
                if (list.size()>0){
                    requestPlayListItem(list);
                }
            }
        });
        api.setChannelId(mContext.getString(R.string.channel_key));
        api.execute();
    }
    private void requestPlayListItem(ArrayList<ChannelSectionEntity> lst){
        final ArrayList<PlayListItemEntity> listData = new ArrayList<>();
        for(int i =0 ; i < lst.size();i++){
            ChannelSectionEntity obj = lst.get(i);
            Random rn = new Random();
            int index = rn.nextInt(obj.contentDetails.playlists.size());
            DebugLog.log("PlayList",obj.contentDetails.playlists.toString());
            DebugLog.log("index_Random",index+"");
            PlayListItemsApi api = new PlayListItemsApi(new ApiListener() {
                @Override
                public void onError(VolleyError statusCode) {
                    DebugLog.log_e(TAG, "requestPlayListItem");

                }

                @Override
                public void onFinish(Object result, boolean endRequest) {
                    DebugLog.log("result",result.toString());
                    ArrayList<PlayListItemEntity> lst = PlayListItemParser.parser(result.toString());
                    listData.addAll(lst);
                    if (endRequest){
                        ((ModelCallBackHome) mCallBack).onLoadNew(listData);
                    }
                }
            });
            api.setPlayListId(obj.contentDetails.playlists.get(index));
            api.setMaxResults("1");
            api.execute();
        }
    }
}
