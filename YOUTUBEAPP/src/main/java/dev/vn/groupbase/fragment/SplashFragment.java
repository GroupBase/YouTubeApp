package dev.vn.groupbase.fragment;

import android.widget.ProgressBar;
import android.widget.TextView;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.activity.BaseActivity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.model.callback.ModelCallBackSplash;
import dev.vn.groupbase.model.SplashModel;
import gmo.hcm.net.lib.RequestError;

/**
 * Created by acnovn on 11/7/16.
 */

public class SplashFragment extends FragmentCommon implements ModelCallBackSplash{
    private SplashModel mModel;
    TextView tv_loaddata;
    ProgressBar progressBar;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void initView() {
        this.mShowToolBar = false;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_loaddata = (TextView) findViewById(R.id.tv_loaddata);
    }

    @Override
    protected ModelCommon initModel() {
        if (mModel == null) {
            mModel = new SplashModel(this);
        }
        return mModel;
    }

    @Override
    public void complete(boolean isComplete) {
        if (isComplete) {
            ViewManager.getInstance().addFragment(MainFragment.newInstance(null, MainFragment.class), false);
        }
    }

    @Override
    public void onError(RequestError error_type) {
        switch (error_type){
            case NETWORK:
            case NETWORK_LOST:
                onShowError();
                break;
        }
    }

    @Override
    public void onReload() {
        mModel.requestChannelSection();
    }
}
