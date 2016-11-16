package dev.vn.groupbase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.activity.PlayListItemsActivity;
import dev.vn.groupbase.adapter.PlayListAdapter;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.model.callback.ModelCallBackPlayList;
import dev.vn.groupbase.model.ChannelSectionsModel;
import dev.vn.groupbase.model.PlayListModel;
import gmo.hcm.net.lib.RequestError;

import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_IMAGE;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;
import static dev.vn.groupbase.model.PlayListModel.PLAY_LIST_KEY;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListFragment extends FragmentCommon implements ModelCallBackPlayList,OnItemClickListener{
    private PlayListModel mModel;
    private List<PlayListEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayListAdapter mAdapter;
    InterstitialAd mInterstitialAd;
    private PlayListEntity obj_sender;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(getString(R.string.admod_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                showVideoList();
            }
        });
        requestNewInterstitial();
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("9F715FB9B5E60D162D735A986D2FA70B")
                .build();

        mInterstitialAd.loadAd(adRequest);
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
    public void onError(RequestError error_type) {

    }

    @Override
    public void onItemClick(View itemView, int position) {
        obj_sender = mAdapter.getObject(position);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            showVideoList();
        }

    }
    private void showVideoList(){
        if (obj_sender!=null) {
            String url_data;
            try {
                if (!TextUtils.isEmpty(obj_sender.snippet.thumbnails.maxres.url)) {
                    url_data = obj_sender.snippet.thumbnails.maxres.url;
                } else if (!TextUtils.isEmpty(obj_sender.snippet.thumbnails.standard.url)) {
                    url_data = obj_sender.snippet.thumbnails.standard.url;
                } else if (!TextUtils.isEmpty(obj_sender.snippet.thumbnails.high.url)) {
                    url_data = obj_sender.snippet.thumbnails.high.url;
                } else if (!TextUtils.isEmpty(obj_sender.snippet.thumbnails.medium.url)) {
                    url_data = obj_sender.snippet.thumbnails.medium.url;
                } else {
                    url_data = obj_sender.snippet.thumbnails.default_url.url;
                }
            } catch (Exception e) {
                url_data = "";
            }
            Intent intent = new Intent(mContext, PlayListItemsActivity.class);
            Bundle data_sender = new Bundle();
            data_sender.putString(PLAY_LIST_KEY, obj_sender.id);
            data_sender.putString(LIST_PLAY_TITLE, obj_sender.snippet.title);
            data_sender.putString(LIST_PLAY_IMAGE, url_data);
            intent.putExtras(data_sender);
            startActivity(intent);
            obj_sender = null;
        } else {
            return;
        }
    }
}
