package app.thn.groupbase.youtubeapp;

import android.os.Bundle;

import dev.vn.groupbase.activity.BaseActivity;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.SplashFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ViewManager.getInstance().addFragment(SplashFragment.newInstance(null,SplashFragment.class),false);
        }
    }
}
