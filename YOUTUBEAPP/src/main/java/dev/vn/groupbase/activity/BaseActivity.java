package dev.vn.groupbase.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.common.ActivityCommon;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.util.Helper;

/**
 * Created by acnovn on 11/1/16.
 */

public abstract class BaseActivity extends ActivityCommon {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setLayOutCommon(R.layout.activity_base);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        initToolBar();
    }
    public void initToolBar() {
        View toolBarView = getLayoutInflater().inflate(R.layout.base_menu_top, null);
        setToolBarView(toolBarView);
    }
    public void setToolBarView(View toolBarView) {
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.addView(toolBarView);
        mToolbar.setContentInsetsAbsolute(0, 0);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }
    public Toolbar getToolbar() {
        return mToolbar;
    }
    public void closeApp(View v){
        ViewManager.getInstance().closeApplication();
    }
    public void showErrorView(){
        findViewById(R.id.ln_error).setVisibility(View.VISIBLE);
    }
    public void showhideView(){
        findViewById(R.id.ln_error).setVisibility(View.GONE);
    }
//    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (Helper.isNetworkConnected(context)){
//                showhideView();
//            } else {
//                showErrorView();
//            }
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(networkStateReceiver);
//    }
}
