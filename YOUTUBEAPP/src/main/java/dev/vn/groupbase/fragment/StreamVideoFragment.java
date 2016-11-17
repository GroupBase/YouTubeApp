package dev.vn.groupbase.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.io.IOException;


import app.thn.groupbase.gameshow.R;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;

import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.listener.StreamVideoListener;
import dev.vn.groupbase.model.StreamVideoModel;
import dev.vn.groupbase.model.callback.ModelCallBackStreamVideo;
import gmo.hcm.net.lib.RequestError;
import vn.amobi.util.ads.video.AmobiShowVideoAdRequest;
import vn.amobi.util.ads.video.AmobiVideoAd;
import vn.amobi.util.ads.video.AmobiVideoAdListener;

/**
 * Created by acnovn on 11/9/16.
 */

public class StreamVideoFragment extends FragmentCommon implements View.OnClickListener, ModelCallBackStreamVideo, MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener ,AmobiVideoAdListener,SurfaceHolder.Callback {
    private static String TAG = "StreamVideoFragment";
    private SurfaceView videoView;
    private MediaPlayer mp;
    private SurfaceHolder holder;
    private SurfaceHolder mFirstSurface;
    private MediaController mController;
    private ImageView thumbnail;
    private String url_thumbnail;
    private StreamVideoModel mModel;
    private StreamVideoListener mStreamVideoListener;
    private boolean isPlay = false;
    private String mVideoUri;
    private boolean loadAd = false;
    private boolean isHavaStream = false;
    private boolean stopAd = false;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stream_video;
    }

    @Override
    protected void initView() {
        AmobiVideoAd.getInstance().setVideoAdListener(this);
        AmobiVideoAd.getInstance().prepare(getActivity());
        videoView = (SurfaceView) findViewById(R.id.videoView);
        thumbnail = (ImageView) findViewById(R.id.iv_thumbnails);
        if (mFirstSurface == null) {
            holder = videoView.getHolder();
        } else {
            holder = mFirstSurface;
        }
        videoView.setOnClickListener(this);
        initPlay();
        holder.addCallback(this);
        mController = new MediaController(mContext);
        final ImageView fullScreenButton = new ImageView(mContext);
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drawable_id = Integer.parseInt(fullScreenButton.getTag().toString());
                if (drawable_id == R.mipmap.ic_fullscreen) {
                    fullScreenButton.setImageResource(R.mipmap.ic_fullscreen_exit);
                    fullScreenButton.setTag(R.mipmap.ic_fullscreen_exit);
                    mStreamVideoListener.onFullScreen();
                } else {
                    fullScreenButton.setImageResource(R.mipmap.ic_fullscreen);
                    fullScreenButton.setTag(R.mipmap.ic_fullscreen);
                    mStreamVideoListener.onExitFullScreen();
                }
            }
        });
        mController.setAnchorView(findViewById(R.id.videoView));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        params.setMargins(0, 0, 10, 0);
        fullScreenButton.setImageResource(R.mipmap.ic_fullscreen);
        fullScreenButton.setTag(R.mipmap.ic_fullscreen);
        mController.addView(fullScreenButton, params);
        mController.setMediaPlayer(StreamVideoFragment.this);
        mController.setEnabled(true);
    }

    private void initPlay(){
        mp = new MediaPlayer();
        mp.setScreenOnWhilePlaying(true);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnPreparedListener(this);
        mp.setOnErrorListener(this);
        mp.setOnCompletionListener(this);
        if (mController!=null){
            mController.setMediaPlayer(StreamVideoFragment.this);
        }

    }
    @Override
    protected ModelCommon initModel() {
        if (mModel == null) {
            mModel = new StreamVideoModel(this);
        }
        return mModel;
    }

    public void setListener(StreamVideoListener listener) {
        mStreamVideoListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoView:
                if (mController != null) {
                    mController.show();
                }
                break;
        }
    }

    public void loadVideo(String videoId, String thumbnails) {
        url_thumbnail = thumbnails;
        isHavaStream = false;
        mStreamVideoListener.onRequestStreamStart();
        if (isPlay) {
            mp.pause();
            isPlay = false;
        }
        mp.reset();
        mModel.requestStream(videoId);
    }

    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.setMargins(0, 0, 0, 0);
            videoView.setLayoutParams(params);
            DebugLog.log_e(TAG, "LANDSCAPE");
        } else {
            setVideoSize();
        }
    }

    @Override
    public void onData(String data) {
        isHavaStream = true;
        String url_Stream = data.toString();
        mVideoUri = url_Stream;
        play(mVideoUri);

    }
    private void play(String url_Stream){
        if (mp == null) {
            return;
        }
        try {
            mp.reset();
            mp.setDataSource(mContext, Uri.parse(url_Stream));
            mp.prepare();
            DebugLog.log_e(TAG, "prepare_OK");
        } catch (IllegalArgumentException e) {
            DebugLog.log_e(TAG, "prepare_error1");

        } catch (IllegalStateException e) {
            DebugLog.log_e(TAG, "prepare_error2");

        } catch (IOException e) {
            DebugLog.log_e(TAG, "prepare_error3");

        }
    }

    @Override
    public void start() {
        isPlay = true;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mp.start();
        thumbnail.setVisibility(View.GONE);
    }

    @Override
    public void pause() {
        if (isPlay) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mp.pause();
        }
    }

    @Override
    public int getDuration() {
        return mp.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mp.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mp.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mp.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setVideoSize() {
        // // Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        DebugLog.log_e(TAG, "onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        DebugLog.log_e(TAG, "setOnErrorListener");
        mStreamVideoListener.onRequestStreamError();
        mp.stop();
        mp.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        DebugLog.log_e(TAG, "onPrepared");
        setVideoSize();
        Glide.with(mContext).load(url_thumbnail)
                .skipMemoryCache(false)
                .into(thumbnail);
        thumbnail.setVisibility(View.VISIBLE);
        mStreamVideoListener.onRequestStreamFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
            DebugLog.log_e(TAG, "onDestroy");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.log_e(TAG, "onResume");
        if (!TextUtils.isEmpty(mVideoUri)&& !isPlay){
            Glide.with(mContext).load(url_thumbnail)
                    .skipMemoryCache(false)
                    .into(thumbnail);
            thumbnail.setVisibility(View.VISIBLE);
            ProgressLoading.show();
            if (!isPlay) {
                if (mp == null) {
                    initPlay();
                }
                play(mVideoUri);
            }

        }
    }

    @Override
    public void onError(RequestError error_type) {
        mStreamVideoListener.onRequestStreamError();
    }

    @Override
    public void onAdAvailable() {
        DebugLog.showToast("onAdAvailable");
        loadAd = true;
        Intent intent = getActivity().getIntent();
        if (intent!=null) {
            if (intent.getExtras().getBoolean("load_ad", false)) {
                loadAd();
                stopAd = true;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlay){
            mp.pause();
        }else {
            if (mp != null) {
                mp.release();
                mp = null;
            }
        }
    }

    @Override
    public void onPrepareError() {
        loadAd = false;
        DebugLog.showToast("onPrepareError");
    }

    @Override
    public void onAdStarted() {
        mStreamVideoListener.onLoadAdStart();
    }

    @Override
    public void onAdFinished() {
        mStreamVideoListener.onLoadAdFinish();
        loadAd = false;
        Intent intent = getActivity().getIntent();
        if (!stopAd) {
            AmobiVideoAd.getInstance().prepare(getActivity());
        }
    }
    public void loadAd(){
        AmobiVideoAd.getInstance().showAd(new AmobiShowVideoAdRequest());
    }
    public boolean isAd(){
        return loadAd;
    }
    public boolean isStream(){
        return isHavaStream;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DebugLog.log_e(TAG, "surfaceCreated");
        mFirstSurface = holder;
        if (mp!=null){
            mp.setDisplay(mFirstSurface);
        }
//        if (mVideoUri != null) {
//            mp.setDisplay(mFirstSurface);
//        } else {
//            mp.setDisplay(holder);
//
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        DebugLog.log_e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        DebugLog.log_e(TAG, "surfaceDestroyed");
//        if (isPlay){
//            mp.pause();
//            isPlay = false;
//        }
    }
}
