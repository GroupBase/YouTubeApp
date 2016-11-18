package dev.vn.groupbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.common.ActivityCommon;

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

}
