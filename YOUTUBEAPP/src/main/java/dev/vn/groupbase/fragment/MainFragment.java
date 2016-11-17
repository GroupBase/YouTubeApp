package dev.vn.groupbase.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import app.thn.groupbase.gameshow.R;
import dev.vn.groupbase.adapter.MainAdapter;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;

/**
 * Created by acnovn on 11/2/16.
 */

public class MainFragment extends FragmentCommon {
    private ViewPager vp_page;
    private TabLayout mHeader;
    private MainAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        this.mShowToolBar = false;
        mAdapter = new MainAdapter(getChildFragmentManager());
        vp_page = (ViewPager) findViewById(R.id.vp_page);
        vp_page.setAdapter(mAdapter);
        mHeader = (TabLayout) findViewById(R.id.sliding_tabs);
        mHeader.setupWithViewPager(vp_page);

        mHeader.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getPosition();
                setTabChange(tab.getPosition(), tab.getCustomView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabUnChange(tab.getPosition(), tab.getCustomView());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setTabChange(tab.getPosition(), tab.getCustomView());
            }
        });
        for (int i = 0; i < mHeader.getTabCount(); i++) {
            TabLayout.Tab tab = mHeader.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
        setTabChange(vp_page.getCurrentItem(), mHeader.getTabAt(vp_page.getCurrentItem()).getCustomView());
    }

    @Override
    protected ModelCommon initModel() {
        return null;
    }

    private void setTabChange(int position, View v) {
        ImageView imageView = (ImageView) v.findViewById(R.id.tab);
        switch (position) {
            case 0:
                imageView.setImageResource(R.mipmap.ic_home_white_36dp);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.ic_list_white_36dp);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.ic_history_white_36dp);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.ic_bookmark_white_36dp);
                break;
        }
    }

    private void setTabUnChange(int position, View v) {
        ImageView imageView = (ImageView) v.findViewById(R.id.tab);
        switch (position) {
            case 0:
                imageView.setImageResource(R.mipmap.ic_home_black_36dp);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.ic_list_black_36dp);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.ic_history_black_36dp);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.ic_bookmark_black_36dp);
                break;
        }
    }
}
