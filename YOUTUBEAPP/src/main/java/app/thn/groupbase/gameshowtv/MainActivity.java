package app.thn.groupbase.gameshowtv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.ads.MobileAds;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.common.ActivityCommon;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.AboutFragment;


public class MainActivity extends ActivityCommon {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(PreferenceManager.newInstance(getApplicationContext()).getAd_start_app())) {
            StartAppSDK.init(this, PreferenceManager.newInstance(getApplicationContext()).getAd_start_app(), true);
        }
        StartAppAd.disableSplash();
        if (!TextUtils.isEmpty(PreferenceManager.newInstance(getApplicationContext()).getAd_admod_init())) {
            MobileAds.initialize(getApplicationContext(), PreferenceManager.newInstance(getApplicationContext()).getAd_admod_init());
        }
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ViewManager.getInstance().addFragment(AboutFragment.newInstance(null, AboutFragment.class), false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
