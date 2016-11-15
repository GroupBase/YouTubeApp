package app.thn.groupbase.youtubeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import dev.vn.groupbase.common.ActivityCommon;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.SplashFragment;

public class MainActivity extends ActivityCommon {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StartAppSDK.init(this, getString(R.string.start_app_id), true);
        StartAppAd.disableSplash();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ViewManager.getInstance().addFragment(SplashFragment.newInstance(null,SplashFragment.class),false);
        }
    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}
