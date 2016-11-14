package dev.vn.groupbase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.activity.PlayListItemsActivity;
import dev.vn.groupbase.adapter.HomeAdapter;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.model.callback.ModelCallBackHome;
import dev.vn.groupbase.model.HomeModel;

import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_IMAGE;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;
import static dev.vn.groupbase.model.PlayListModel.PLAY_LIST_KEY;

/**
 * Created by acnovn on 10/26/16.
 */

public class HomeFragment extends FragmentCommon implements ModelCallBackHome, OnItemClickListener {
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
        this.mShowToolBar = false;
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
        mAdapter.setListener(this);
    }

    @Override
    public void onError(ModelCommon.ERROR_TYPE error_type) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        PlayListItemEntity obj = mAdapter.getObject(position);
        String url_data;
        try {
            if (!TextUtils.isEmpty(obj.snippet.thumbnails.maxres.url)) {
                url_data = obj.snippet.thumbnails.maxres.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.standard.url)) {
                url_data = obj.snippet.thumbnails.standard.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.high.url)) {
                url_data = obj.snippet.thumbnails.high.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.medium.url)) {
                url_data = obj.snippet.thumbnails.medium.url;
            } else {
                url_data = obj.snippet.thumbnails.default_url.url;
            }
        } catch (Exception e){
            url_data = "";
        }
        Intent intent = new Intent(mContext, PlayListItemsActivity.class);
        Bundle data = new Bundle();
        data.putString(PLAY_LIST_KEY,obj.snippet.playlistId);
        data.putString(LIST_PLAY_TITLE,obj.snippet.title);
        data.putString(LIST_PLAY_IMAGE,url_data);
        intent.putExtras(data);
        startActivity(intent);
    }
}
