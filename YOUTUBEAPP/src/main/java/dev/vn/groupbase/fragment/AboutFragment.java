package dev.vn.groupbase.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.thn.groupbase.gameshowtv.BuildConfig;
import app.thn.groupbase.gameshowtv.R;
import dev.vn.groupbase.PreferenceManager;
import dev.vn.groupbase.api.entity.YouTubeEntity;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.model.AboutModel;
import dev.vn.groupbase.model.callback.ModelCallBackAbout;
import gmo.hcm.net.lib.RequestError;

/**
 * Created by nghiath on 11/16/16.
 */

public class AboutFragment extends FragmentCommon implements ModelCallBackAbout{
    private AboutModel mModel;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView() {
//        if (BuildConfig.DEBUG) {
//            if (!TextUtils.isEmpty(PreferenceManager.newInstance(ViewManager.getInstance().getActivity()).getAd_admod_key())) {
//                AdView mAdView = (AdView) findViewById(R.id.adView);
//                mAdView.setVisibility(View.VISIBLE);
//                mAdView.setAdUnitId(PreferenceManager.newInstance(ViewManager.getInstance().getActivity()).getAd_admod_key());
//                AdRequest adRequest = new AdRequest.Builder().build();
//                mAdView.loadAd(adRequest);
//            }
//        }

        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.checkApp();
            }
        });
        TextView infor = (TextView)findViewById(R.id.tv_info);
        infor.setText(String.format(getString(R.string.infor),getString(R.string.app_name)));
    }

    @Override
    protected ModelCommon initModel() {
        if (mModel == null){
            mModel= new AboutModel(this);
        }
        return mModel;
    }

    @Override
    public void onData(YouTubeEntity data) {
        if (data.status == 0){
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.channel_address))));

            ViewManager.getInstance().closeApplication();
        } else if (data.status == 1){

            ViewManager.getInstance().addFragment(SplashFragment.newInstance(null, SplashFragment.class), false);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(mContext.getString(R.string.dialog_title));
            dialog.setMessage(mContext.getString(R.string.dialog_message));
            dialog.setCancelable(false);
            dialog.setPositiveButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ViewManager.getInstance().closeApplication();
                }
            });
            dialog.create();
            dialog.show();
        }
    }

    @Override
    public void onError(RequestError error_type) {
        switch (error_type){
            case NETWORK:
            case NETWORK_LOST:
                onShowError();
                break;
        }
    }
}
