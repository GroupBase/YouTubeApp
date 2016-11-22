package dev.vn.groupbase.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.adapter.PlayListItemsAdapter;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.database.BookMarkTable;
import dev.vn.groupbase.database.HistoryTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.listener.OnItemClickListener;
import dev.vn.groupbase.model.callback.ModelCallBackPlayListItems;
import dev.vn.groupbase.listener.StreamVideoListener;
import dev.vn.groupbase.model.PlayListItemsModel;
import gmo.hcm.net.lib.RequestError;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_IMAGE;
import static dev.vn.groupbase.model.PlayListModel.LIST_PLAY_TITLE;

/**
 * Created by acnovn on 10/31/16.
 */

public class PlayListItemsFragment extends FragmentCommon implements ModelCallBackPlayListItems, OnItemClickListener, StreamVideoListener {
    private PlayListItemsModel mModel;
    private List<PlayListItemEntity> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayListItemsAdapter mAdapter;
    private StreamVideoFragment videoFragment;
    private View videoBox;
    private String imagePlayList = "";
    ImageView close_button;
    public String titlePlayList;
    private ImageView iv_bookMark, iv_share;
    private BookMarkTable bookMarkTable;
    private int clickCount = 0;
    private View main_view;
    private String mVideoID = "";
    private boolean isFullScreen = false;
    private View.OnLayoutChangeListener listener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(final View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            videoBox.removeOnLayoutChangeListener(listener);
            recyclerView.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.animate().setListener(null);
                    setLayoutSize(recyclerView, MATCH_PARENT, recyclerView.getHeight() - v.getHeight());
                    ProgressLoading.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).translationY(v.getHeight()).setDuration(700);


        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist_items;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        iv_bookMark = (ImageView) findViewById(R.id.iv_bookMark);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        imagePlayList = getArguments().getString(LIST_PLAY_IMAGE, "");
        close_button = (ImageView) findViewById(R.id.close_button);
        titlePlayList = getArguments().getString(LIST_PLAY_TITLE, "");
        iv_bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookMarkTable != null) {
                    YouTubeAppManager.insertBookMark(bookMarkTable);
                    iv_bookMark.setVisibility(View.GONE);
                }
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(mVideoID))) {
                    return;
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_link), mVideoID));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_title)));
            }
        });
        main_view = findViewById(R.id.main_view);

    }

    @Override
    protected ModelCommon initModel() {
        if (mModel == null) {
            mModel = new PlayListItemsModel(this);
        }
        return mModel;
    }

    @Override
    public void setVisibilityActionBar() {
        mToolbarLeft.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(LIST_PLAY_TITLE)) {
                ((TextView) mToolbarLeft.findViewById(R.id.tv_title)).setText(bundle.getString(LIST_PLAY_TITLE));
            }
        }
    }

    @Override
    public void onLoadData(List<PlayListItemEntity> list) {
        lst = list;
        if (!TextUtils.isEmpty(mModel.nextPageToken)) {
            lst.add(null);
        }
        mAdapter = new PlayListItemsAdapter(lst, getContext(), this);
        mAdapter.setLoadMoreListener(this);
        videoFragment =
                (StreamVideoFragment) getChildFragmentManager().findFragmentById(R.id.video_fragment_container);
        videoFragment.setListener(this);
        videoBox = findViewById(R.id.video_box);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_bookMark.setVisibility(View.GONE);
                setLayoutSize(recyclerView, MATCH_PARENT, MATCH_PARENT);
                bookMarkTable = null;
                recyclerView.animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        videoBox.setVisibility(View.INVISIBLE);
                        recyclerView.animate().setListener(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).translationY(0).setDuration(700);
                mAdapter.setSelect(false);
                mAdapter.notifyDataSetChanged();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                videoFragment.pause();
            }
        });
        findViewById(R.id.video_box).setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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

    private void checkBookMark(String videoID, String playListId) {
        boolean isExits = YouTubeAppManager.checkExitsBookMark(videoID, playListId);
        if (!isExits) {
            iv_bookMark.setVisibility(View.VISIBLE);
        } else {
            iv_bookMark.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        mAdapter.setIndexSelect(position);
        mAdapter.setSelect(true);
        PlayListItemEntity obj = mAdapter.getItemObject(position);
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
            mVideoID = videoId;
            int showAd = clickCount % 3;
            DebugLog.showToast(showAd + "");
            if (videoFragment.isAd() && (showAd == 0 || showAd == 3)) {
                ProgressLoading.dismiss();
                videoFragment.loadAd();
            }
            videoFragment.loadVideo(videoId, url);
            clickCount++;
        }
        HistoryTable historyTable = new HistoryTable();
        historyTable.videoId = videoId;
        historyTable.videoName = obj.snippet.title;
        historyTable.playListId = obj.snippet.playlistId;
        historyTable.playListName = getArguments().getString(LIST_PLAY_TITLE);
        historyTable.imgPlayList = imagePlayList;
        historyTable.imgVideo = url;
        YouTubeAppManager.insertHistory(historyTable);
        bookMarkTable = new BookMarkTable();
        bookMarkTable.videoId = videoId;
        bookMarkTable.videoName = obj.snippet.title;
        bookMarkTable.playListId = obj.snippet.playlistId;
        bookMarkTable.playListName = getArguments().getString(LIST_PLAY_TITLE);
        bookMarkTable.imgPlayList = imagePlayList;
        bookMarkTable.imgVideo = url;
        mAdapter.notifyDataSetChanged();
    }

    public void setLayoutSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

    @Override
    public void onRequestStreamStart() {
        ProgressLoading.show();
    }

    @Override
    public void onRequestStreamError(RequestError error_type) {
        ProgressLoading.dismiss();
        switch (error_type) {
            case NETWORK:
            case NETWORK_LOST:
                onShowError();
                break;
        }
    }

    @Override
    public void onRequestStreamFinish() {
        DebugLog.log_e("PlayListItemsFragment", "onRequestStreamFinish");
        if (videoBox.getVisibility() != View.VISIBLE ) {
            videoBox.addOnLayoutChangeListener(listener);
        } else {
            ProgressLoading.dismiss();
        }
        videoBox.setVisibility(View.VISIBLE);
        if (bookMarkTable != null) {
            checkBookMark(bookMarkTable.videoId, bookMarkTable.playListId);
        }
    }

    @Override
    public void onFullScreen() {
        isFullScreen = true;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hideToolBar();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        recyclerView.setVisibility(View.GONE);
        close_button.setVisibility(View.GONE);
        videoFragment.setFullScreen(true);
        setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
        iv_bookMark.setVisibility(View.GONE);
    }

    @Override
    public void onExitFullScreen() {
        isFullScreen = false;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showToolBar();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView.setVisibility(View.VISIBLE);
        close_button.setVisibility(View.VISIBLE);
        videoFragment.setFullScreen(false);
        setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.TOP);
        setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
        if (bookMarkTable != null) {
            checkBookMark(bookMarkTable.videoId, bookMarkTable.playListId);
        }
    }

    @Override
    public void onLoadAdFinish() {
        if (!videoFragment.isStream()) {
            ProgressLoading.show();
        } else {
            ProgressLoading.dismiss();
        }
    }

    @Override
    public void onLoadAdStart() {
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
    public void onLoadMore() {
        DebugLog.showToast("Load More");
        if (!TextUtils.isEmpty(mModel.nextPageToken)) {
            mModel.requestPlayListMore();
        }
    }

    @Override
    public void onReload() {
        mModel.requestPlayList();
    }

    @Override
    public void onBackPress() {
        if (isFullScreen) {
            onExitFullScreen();
        } else {
            super.onBackPress();
        }
    }
}
