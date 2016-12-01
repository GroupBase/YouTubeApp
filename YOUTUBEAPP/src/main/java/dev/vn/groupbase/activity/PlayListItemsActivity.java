package dev.vn.groupbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.phimkinhdi.BuildConfig;
import app.thn.groupbase.phimkinhdi.R;
import dev.vn.groupbase.adapter.PlayListItemsAdapter;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ReloadListener;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.database.BookMarkTable;
import dev.vn.groupbase.database.HistoryTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.fragment.PlayListItemsFragment;
import dev.vn.groupbase.listener.LoadMoreListener;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.model.PlayListItemsModel;
import dev.vn.groupbase.model.PlayListModel;
import dev.vn.groupbase.model.callback.ModelCallBackPlayListItems;
import gmo.hcm.net.lib.RequestError;

import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListItemsActivity extends BaseActivity implements ModelCallBackPlayListItems, OnItemClickListener,ReloadListener ,LoadMoreListener {
    private PlayListItemsModel mModel;
    private List<PlayListItemEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayListItemsAdapter mAdapter;
    private String mTitle = "";
    InterstitialAd mInterstitialAd;
    private int actionView = 0;
    private PlayListItemEntity obj;
    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        Bundle bundle ;
        Intent intent = getIntent();
        bundle = intent.getExtras();
        actionView = bundle.getInt("action_view",0);
        if (actionView == 1){
            setReloadListener(this);
            ((FrameLayout) findViewById(R.id.content)).addView(LayoutInflater.from(this).inflate(R.layout.fragment_playlist_items, null));
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
                mModel = new PlayListItemsModel(this);
            }
            mModel.getData(bundle);
            mModel.requestPlayList();
            getSupportActionBar().show();
            createToolBar();
        } else {
            if (savedInstanceState == null){
                ViewManager.getInstance().addFragment(PlayListItemsFragment.newInstance(bundle,PlayListItemsFragment.class),false);
            }
        }
    }
    private void requestNewInterstitial() {
        if (mInterstitialAd!=null) {
            if (BuildConfig.DEBUG) {
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("73C26E9772F06EC5DBD172AFB699FFC8").build();
                mInterstitialAd.loadAd(adRequest);
            } else {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        }

    }
    private void showVideoList() {
        String url = "";
        try {
            if (!TextUtils.isEmpty(obj.snippet.thumbnails.maxres.url)) {
                url = obj.snippet.thumbnails.maxres.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.standard.url)) {
                url = obj.snippet.thumbnails.standard.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.high.url)) {
                url = obj.snippet.thumbnails.high.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.medium.url)) {
                url = obj.snippet.thumbnails.medium.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.default_url.url)) {
                url = obj.snippet.thumbnails.default_url.url;
            }
        } catch (Exception e) {
            url = "";
        }
        BookMarkTable bookMarkTable = new BookMarkTable();
        bookMarkTable.videoId = obj.contentDetails.videoId;
        bookMarkTable.playListId = obj.snippet.playlistId;
        bookMarkTable.playListName = mTitle;
        bookMarkTable.imgPlayList = url;
        bookMarkTable.videoName = obj.snippet.title;
        bookMarkTable.imgVideo = url;
        Bundle bundle = new Bundle();
        bundle.putString("video_id", bookMarkTable.videoId);
        bundle.putString("url_img", url);
        bundle.putString("title", bookMarkTable.videoName);
        bundle.putSerializable("bookmark", bookMarkTable);
        bundle.putBoolean("load_ad", true);
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public void onItemClick(View itemView, int position) {
        mAdapter.setIndexSelect(position);
        mAdapter.setSelect(true);
        obj = mAdapter.getItemObject(position);
        String videoId = mAdapter.getVideoId(position);
        String url = "";
        try {
            if (!TextUtils.isEmpty(obj.snippet.thumbnails.maxres.url)) {
                url = obj.snippet.thumbnails.maxres.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.standard.url)) {
                url = obj.snippet.thumbnails.standard.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.high.url)) {
                url = obj.snippet.thumbnails.high.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.medium.url)) {
                url = obj.snippet.thumbnails.medium.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.default_url.url)) {
                url = obj.snippet.thumbnails.default_url.url;
            }
        } catch (Exception e) {
            url = "";
        }
        if (!TextUtils.isEmpty(videoId)) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                showVideoList();
            }
        }
        HistoryTable historyTable = new HistoryTable();
        historyTable.videoId = videoId;
        historyTable.videoName = obj.snippet.title;
        historyTable.playListId = obj.snippet.playlistId;
        historyTable.playListName = mTitle;
        historyTable.imgPlayList = url;
        historyTable.imgVideo = url;
        YouTubeAppManager.insertHistory(historyTable);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadData(List<PlayListItemEntity> list) {
        lst = list;
        if (!TextUtils.isEmpty(mModel.nextPageToken)) {
            lst.add(null);
        }
        mAdapter = new PlayListItemsAdapter(lst, this, this);
        mAdapter.setLoadMoreListener(this);
        mAdapter.setSelect(false);
        mAdapter.notifyDataSetChanged();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadNext(List<PlayListItemEntity> list) {
        lst.remove(lst.size() - 1);
        lst.addAll(list);
        if (!TextUtils.isEmpty(mModel.nextPageToken)) {
            lst.add(null);
        }
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onLoadMore() {
        DebugLog.showToast("Load More");
        if (!TextUtils.isEmpty(mModel.nextPageToken)) {
            mModel.requestPlayListMore();
        }
    }
    protected void createToolBar() {
        ViewGroup mToolBarView = (ViewGroup) getLayoutInflater().inflate(R.layout.base_menu_top,
                null);
        View mToolbarLeft = getToolbar().findViewById(R.id.mn_left);
        mToolbarLeft.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(PlayListModel.LIST_PLAY_TITLE)) {
                ((TextView) mToolbarLeft.findViewById(R.id.tv_title)).setText(bundle.getString(PlayListModel.LIST_PLAY_TITLE));
                mTitle =bundle.getString(PlayListModel.LIST_PLAY_TITLE);
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
    protected void onResume() {
        super.onResume();
        requestNewInterstitial();
    }
}
