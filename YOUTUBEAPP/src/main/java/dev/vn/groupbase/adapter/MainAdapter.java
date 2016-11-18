package dev.vn.groupbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.BookMarkFragment;
import dev.vn.groupbase.fragment.ChannelSectionsFragment;
import dev.vn.groupbase.fragment.HistoryFragment;
import dev.vn.groupbase.fragment.HomeFragment;

/**
 * Created by acnovn on 11/1/16.
 */

public class MainAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public MainAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        list.add(HomeFragment.newInstance(null, HomeFragment.class));
        list.add(ChannelSectionsFragment.newInstance(null, ChannelSectionsFragment.class));
        list.add(HistoryFragment.newInstance(null, HistoryFragment.class));
        list.add(BookMarkFragment.newInstance(null, BookMarkFragment.class));
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(ViewManager.getInstance().getActivity()).inflate(R.layout.item_tab, null);
        ImageView tab = (ImageView) v.findViewById(R.id.tab);
        switch (position) {
            case 0:
                tab.setImageResource(R.mipmap.ic_home_black_36dp);
                break;
            case 1:
                tab.setImageResource(R.mipmap.ic_list_black_36dp);
                break;
            case 2:
                tab.setImageResource(R.mipmap.ic_history_black_36dp);
                break;
            case 3:
                tab.setImageResource(R.mipmap.ic_bookmark_black_36dp);
                break;
        }
        return v;
    }
}
