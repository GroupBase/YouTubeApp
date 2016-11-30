package dev.vn.groupbase.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.thn.groupbase.phimkinhdi.R;
import gmo.hcm.net.lib.NetworkUtil;


/**
 * Created by acnovn on 10/14/16.
 */

public abstract class ActivityCommon extends AppCompatActivity {
    protected int mLayoutCommon = 0;
    private ReloadListener reloadListener;

    public void setReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.log(this.getClass().getSimpleName(),"onCreate");
        if (mLayoutCommon == 0 ) {
            setContentView(R.layout.activity_common);
        } else {
            setContentView(mLayoutCommon);
        }
        ViewManager.getInstance().setActivity(this);
        initView();
        onCreateExecute(savedInstanceState);
    }

    protected void initView(){

    }
    protected void setLayOutCommon(int layoutCommon){
        mLayoutCommon = layoutCommon;
    }
    @Override
    protected void onStart() {
        super.onStart();
        ViewManager.getInstance().startActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewManager.getInstance().setActivity(this);
        ViewManager.getInstance().resumeActivity();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewManager.getInstance().pauseActivity();
        unregisterReceiver(networkStateReceiver);

    }

    @Override
    protected void onStop() {
        ViewManager.getInstance().stopActivity();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().destroyActivity();
    }

    @Override
    public void onBackPressed() {
        if (ViewManager.getInstance().getCurrentFragment() != null) {
            ViewManager.getInstance().getCurrentFragment().onBackPress();
        } else {
            super.onBackPressed();
        }
    }

    protected abstract void onCreateExecute(Bundle savedInstanceState);
    public void closeApp(View v) {
        ViewManager.getInstance().closeApplication();
    }

    public void reloadData(View v) {
        if (NetworkUtil.isNetworkConnected(this)) {
            showHideView();
            if (reloadListener!=null) {
                reloadListener.onReload();
            }
        } else {
            showErrorView();
        }
    }

    public void showErrorView() {
        ProgressLoading.dismiss();
        findViewById(R.id.ln_error).setVisibility(View.VISIBLE);
        findViewById(R.id.content).setVisibility(View.GONE);
    }

    public void showHideView() {
        findViewById(R.id.ln_error).setVisibility(View.GONE);
        findViewById(R.id.content).setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtil.isNetworkConnected(context)){
                if (reloadListener!=null) {
                    reloadListener.onShowError();
                }
            }
        }
    };

}
