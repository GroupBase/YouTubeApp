package dev.vn.groupbase.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.adapter.HomeAdapter;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.model.callback.ModelCallBackHome;
import dev.vn.groupbase.model.HomeModel;

/**
 * Created by acnovn on 10/26/16.
 */

public class HomeFragment extends FragmentCommon implements ModelCallBackHome {
    private List<PlayListItemEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    private HomeModel mHomeModel;
    private boolean isFirstLoadData = false;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (isFirstLoadData) {
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected ModelCommon initModel() {
        mHomeModel = new HomeModel(this);
        return mHomeModel;
    }


    @Override
    public void onLoadNew(ArrayList<PlayListItemEntity> list) {
        lst = list;
        isFirstLoadData = true;
        mAdapter = new HomeAdapter(lst,getActivity());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onError(ModelCommon.ERROR_TYPE error_type) {

    }
}
