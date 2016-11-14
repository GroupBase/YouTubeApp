package dev.vn.groupbase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.activity.PlayListItemsActivity;
import dev.vn.groupbase.adapter.PlayListAdapter;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.listener.PlayListListener;
import dev.vn.groupbase.model.ChannelSectionsModel;
import dev.vn.groupbase.model.PlayListModel;

import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_IMAGE;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;
import static dev.vn.groupbase.model.PlayListModel.PLAY_LIST_KEY;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListFragment extends FragmentCommon implements PlayListListener ,OnItemClickListener{
    private PlayListModel mModel;
    private List<PlayListEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayListAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
    }

    @Override
    protected ModelCommon initModel() {
        if (mModel==null){
            mModel = new PlayListModel(this);
        }
        return mModel;
    }

    @Override
    public void setVisibilityActionBar() {
        mToolbarLeft.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        if (bundle!=null){
            if(bundle.containsKey(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE)) {
                ((TextView)mToolbarLeft.findViewById(R.id.tv_title)).setText(bundle.getString(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE));
            }
        }

    }

    @Override
    public void onLoadData(List<PlayListEntity> list) {
        lst = list;
        mAdapter = new PlayListAdapter(lst,getContext());
        mAdapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onError(ModelCommon.ERROR_TYPE error_type) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        PlayListEntity obj = mAdapter.getObject(position);
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
        data.putString(PLAY_LIST_KEY,obj.id);
        data.putString(LIST_PLAY_TITLE,obj.snippet.title);
        data.putString(LIST_PLAY_IMAGE,url_data);
        intent.putExtras(data);
        startActivity(intent);
    }
}
