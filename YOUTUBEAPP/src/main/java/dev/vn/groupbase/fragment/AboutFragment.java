package dev.vn.groupbase.fragment;

import android.view.View;
import android.widget.TextView;

import app.thn.groupbase.gameshow.R;
import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ViewManager;

/**
 * Created by nghiath on 11/16/16.
 */

public class AboutFragment extends FragmentCommon {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.newInstance().setShowAbout(true);
                ViewManager.getInstance().addFragment(SplashFragment.newInstance(null, SplashFragment.class), false);
            }
        });
        TextView infor = (TextView)findViewById(R.id.tv_info);
        infor.setText(String.format(getString(R.string.infor),getString(R.string.app_name)));
    }

    @Override
    protected ModelCommon initModel() {
        return null;
    }
}
