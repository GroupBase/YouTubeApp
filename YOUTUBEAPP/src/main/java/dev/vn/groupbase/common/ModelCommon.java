package dev.vn.groupbase.common;

import android.content.Context;
import android.os.Bundle;

import dev.vn.groupbase.api.entity.PageInfoEntity;

/**
 * Created by acnovn on 10/15/16.
 */

public class ModelCommon {
    protected Context mContext;
    protected ModelCallBack mCallBack;
    public String prevPageToken;
    public String nextPageToken;
    public PageInfoEntity pageInfo;
    public ModelCommon() {
    }
    public ModelCommon(ModelCallBack callBack) {
        this.mCallBack = callBack;
        this.mContext = ViewManager.getInstance().getActivity();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void getData(Bundle bundle) {

    }
}
