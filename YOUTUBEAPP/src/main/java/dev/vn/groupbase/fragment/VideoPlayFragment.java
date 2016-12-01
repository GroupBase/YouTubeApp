package dev.vn.groupbase.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.thn.groupbase.phimkinhdi.R;
import dev.vn.groupbase.api.entity.VideoEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.database.BookMarkTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.listener.StreamVideoListener;
import dev.vn.groupbase.model.VideoPlayModel;
import dev.vn.groupbase.model.callback.ModelCallBackVideoPlay;
import gmo.hcm.net.lib.RequestError;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by acnovn on 11/3/16.
 */

public class VideoPlayFragment extends FragmentCommon implements StreamVideoListener, ModelCallBackVideoPlay {
    private StreamVideoFragment videoFragment;
    private String videoId, url_img;
    private View videoBox, ln_videoContent;
    private ImageView iv_bookMark, iv_share;
    private BookMarkTable bookMarkTable;
    private VideoPlayModel mModel;
    private TextView tv_description, tv_title;
    private static final String URL_REGEX = new String("http[s]?://[\\w\\d/%#$&?()~_.=+-]+");
    private View.OnLayoutChangeListener listener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            videoBox.removeOnLayoutChangeListener(listener);
            ln_videoContent.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ln_videoContent.animate().setListener(null);
                    ProgressLoading.dismiss();
                    if (videoFragment.isAd()) {
                        videoFragment.loadAd();
                    }
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
        return R.layout.fragment_video_play;
    }

    @Override
    protected void initView() {
        videoFragment =
                (StreamVideoFragment) getChildFragmentManager().findFragmentById(R.id.video_fragment_container);
        videoBox = findViewById(R.id.video_box);
        iv_bookMark = (ImageView) findViewById(R.id.iv_bookMark);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        ln_videoContent = findViewById(R.id.ln_videoContent);
        videoFragment.setListener(this);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_title = (TextView) findViewById(R.id.tv_title);
        videoId = getArguments().getString("video_id", "");
        url_img = getArguments().getString("url_img", "");
        BookMarkTable obj = (BookMarkTable) getArguments().getSerializable("bookmark");
        bookMarkTable = new BookMarkTable();
        if (obj != null) {
            bookMarkTable = obj;
        } else {
            bookMarkTable.imgPlayList = "";
            bookMarkTable.imgVideo = url_img;
            bookMarkTable.videoId = videoId;
            bookMarkTable.playListId = "9999";
            bookMarkTable.playListName = "Video";
            bookMarkTable.videoName = "";
        }
        mModel.requestVideo(videoId);
        if (!TextUtils.isEmpty(videoId)) {
            videoFragment.loadVideo(videoId, url_img);
        }

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
                if ((TextUtils.isEmpty(videoId))) {
                    return;
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_link), videoId));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_title)));
            }
        });
    }

    @Override
    protected ModelCommon initModel() {
        if (mModel == null) {
            mModel = new VideoPlayModel(this);
        }
        return mModel;
    }

    @Override
    public void setVisibilityActionBar() {
        mToolbarLeft.setVisibility(View.VISIBLE);
        TextView title = (TextView) mToolbarLeft.findViewById(R.id.tv_title);
        title.setVisibility(View.VISIBLE);
        title.setText(getArguments().getString("title", ""));
    }

    @Override
    public void onRequestStreamStart() {
        ProgressLoading.show();

    }

    @Override
    public void onRequestStreamError(RequestError error_type) {
        ProgressLoading.dismiss();
        switch (error_type){
            case NETWORK:
            case NETWORK_LOST:
                onShowError();
                break;
        }
    }

    @Override
    public void onRequestStreamFinish() {
        if (videoBox.getVisibility() != View.VISIBLE) {
            videoBox.addOnLayoutChangeListener(listener);
        } else {
            ProgressLoading.dismiss();
//            if (videoFragment.isAd()) {
//                videoFragment.loadAd();
//            }
        }
        videoBox.setVisibility(View.VISIBLE);

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
    public void onFullScreen() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hideToolBar();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ln_videoContent.setVisibility(View.GONE);
        videoFragment.setFullScreen(true);
        setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
        iv_bookMark.setVisibility(View.GONE);
    }

    @Override
    public void onExitFullScreen() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showToolBar();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ln_videoContent.setVisibility(View.VISIBLE);
        videoFragment.setFullScreen(false);
        setLayoutSize(ln_videoContent, MATCH_PARENT, MATCH_PARENT);
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
    public void onData(VideoEntity data) {
        if (data == null) {
            tv_title.setText("");
            tv_description.setText("");
        } else {
            VideoEntity obj = data;
            tv_title.setText(obj.snippet.title);
            bookMarkTable.videoName = obj.snippet.title;
            Pattern pattern = Pattern.compile(URL_REGEX);
            final Matcher matcher = pattern.matcher(obj.snippet.description);
            Spannable spannable = new SpannableString(obj.snippet.description);
            // URLにアンカーを設定
            while (matcher.find()) {
                CustomUrlSpan urlSpan = new CustomUrlSpan(matcher.group(), ViewManager.getInstance().getActivity());
                spannable.setSpan(urlSpan, matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv_description.setText(spannable, TextView.BufferType.SPANNABLE);
            tv_description.setMovementMethod(LinkMovementMethod.getInstance());
            if (bookMarkTable != null) {
                checkBookMark(bookMarkTable.videoId, bookMarkTable.playListId);
            }
        }
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

        if (!TextUtils.isEmpty(videoId)) {
            mModel.requestVideo(videoId);
            videoFragment.loadVideo(videoId, url_img);
        }
    }

    //
    private static class CustomUrlSpan extends URLSpan {

        /**
         * 遷移先Url.
         */
        private String mNoticeUrl;
        /**
         * 遷移元Activity.
         */
        private FragmentActivity mActivity;

        /**
         * コンストラクタ.
         *
         * @param url      遷移先Url.
         * @param activity 遷移元Activity.
         */
        public CustomUrlSpan(final String url, final FragmentActivity activity) {
            super(url);
            mNoticeUrl = url;
            mActivity = activity;
        }

        /*
         * (non-Javadoc)
         * @see android.text.style.URLSpan#onClick(android.view.View)
         */
        @Override
        public void onClick(final View widget) {
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mNoticeUrl)));
        }
    }
}
