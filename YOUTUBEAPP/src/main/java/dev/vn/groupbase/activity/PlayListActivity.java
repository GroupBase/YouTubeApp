package dev.vn.groupbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.ads.MobileAds;

import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.PlayListFragment;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(PreferenceManager.newInstance(getApplicationContext()).getAd_admod_init())) {
            MobileAds.initialize(getApplicationContext(), PreferenceManager.newInstance(getApplicationContext()).getAd_admod_init());
        }
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle ;
            Intent intent = getIntent();
            bundle = intent.getExtras();
            ViewManager.getInstance().addFragment( PlayListFragment.newInstance(bundle,PlayListFragment.class),false);
        } else {

        }
    }
}
