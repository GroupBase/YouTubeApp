package dev.vn.groupbase.common;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by acnovn on 10/15/16.
 */

public class ModelCommon {
    protected Context mContext;
    protected ModelCallBack mCallBack;
    public enum ERROR_TYPE{
        NETWORK
    }
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
