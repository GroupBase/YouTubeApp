package dev.vn.groupbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ExpandableListView;

import java.util.List;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.adapter.HistoryAdapter;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.database.HistoryTable;
import dev.vn.groupbase.database.YouTubeAppManager;

/**
 * Created by acnovn on 11/1/16.
 */

public class HistoryFragment extends FragmentCommon {
    private ExpandableListView mListl;
    private HistoryAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initView() {
        this.mShowToolBar = false;
        mListl = (ExpandableListView) findViewById(R.id.ex_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<HistoryTable> lst = YouTubeAppManager.getHistory();
        if (lst.size()>0) {
            adapter = new HistoryAdapter(mContext, lst);
            mListl.setAdapter(adapter);
        }
    }

    @Override
    protected ModelCommon initModel() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<HistoryTable> lst = YouTubeAppManager.getHistory();
        if (lst.size()>0) {
            adapter = new HistoryAdapter(mContext, lst);
            mListl.setAdapter(adapter);
        }
    }

}
