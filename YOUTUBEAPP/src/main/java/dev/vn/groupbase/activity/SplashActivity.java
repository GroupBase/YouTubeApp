package dev.vn.groupbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.ads.MobileAds;

import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.PlayListFragment;
import dev.vn.groupbase.fragment.SplashFragment;

/**
 * Created by acnovn on 10/27/16.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ViewManager.getInstance().addFragment(SplashFragment.newInstance(null, SplashFragment.class), false);
        }
    }
}
