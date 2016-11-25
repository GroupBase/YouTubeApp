package dev.vn.groupbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.gameshowtv.BuildConfig;
import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.adapter.PlayListAdapter;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.common.ReloadListener;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.PlayListFragment;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.model.ChannelSectionsModel;
import dev.vn.groupbase.model.PlayListModel;
import dev.vn.groupbase.model.callback.ModelCallBackPlayList;
import gmo.hcm.net.lib.RequestError;

import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_IMAGE;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;
import static dev.vn.groupbase.model.PlayListModel.PLAY_LIST_KEY;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListActivity extends BaseActivity implements ModelCallBackPlayList, OnItemClickListener, ReloadListener {
    private PlayListModel mModel;
    private List<PlayListEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayListAdapter mAdapter;
    InterstitialAd mInterstitialAd;
    private PlayListEntity obj_sender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        setReloadListener(this);
        ((FrameLayout) findViewById(R.id.content)).addView(LayoutInflater.from(this).inflate(R.layout.fragment_playlist, null));
        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_admod_init));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_admod_interstitial));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                showVideoList();
            }
        });
        requestNewInterstitial();
        if (mModel == null) {
            mModel = new PlayListModel(this);
        }
        Bundle bundle;
        Intent intent = getIntent();
        bundle = intent.getExtras();
        mModel.getData(bundle);
        mModel.requestPlayList();
        getSupportActionBar().show();
        createToolBar();
//        if (savedInstanceState == null) {
//            Bundle bundle ;
//            Intent intent = getIntent();
//            bundle = intent.getExtras();
//            ViewManager.getInstance().addFragment( PlayListFragment.newInstance(bundle,PlayListFragment.class),false);
//        } else {
//
//        }
    }

    private void showVideoList() {
        if (obj_sender != null) {
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
            Intent intent = new Intent(this, PlayListItemsActivity.class);
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
//        if (BuildConfig.DEBUG) {
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice("73C26E9772F06EC5DBD172AFB699FFC8").build();
//            mInterstitialAd.loadAd(adRequest);
//        } else {
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mInterstitialAd.loadAd(adRequest);
//        }

    }

    @Override
    public void onError(RequestError error_type) {
        switch (error_type) {
            case NETWORK:
            case NETWORK_LOST:
                onShowError();
                break;
        }
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

    @Override
    public void onLoadData(List<PlayListEntity> list) {
        lst = list;
        mAdapter = new PlayListAdapter(lst, this);
        mAdapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    protected void createToolBar() {
        ViewGroup mToolBarView = (ViewGroup) getLayoutInflater().inflate(R.layout.base_menu_top,
                null);
        View mToolbarLeft = getToolbar().findViewById(R.id.mn_left);
        mToolbarLeft.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE)) {
                ((TextView) mToolbarLeft.findViewById(R.id.tv_title)).setText(bundle.getString(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE));
            }
        }
        mToolbarLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });

    }

    @Override
    public void onReload() {
        mModel.requestPlayList();
    }

    @Override
    public void onShowError() {
        showErrorView();
    }

    @Override
    public void onHideError() {
        showHideView();
    }
}
