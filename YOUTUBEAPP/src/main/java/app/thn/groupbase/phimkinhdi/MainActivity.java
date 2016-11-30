package app.thn.groupbase.phimkinhdi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import dev.vn.groupbase.activity.SplashActivity;
import dev.vn.groupbase.api.entity.YouTubeEntity;
import dev.vn.groupbase.common.ActivityCommon;
import dev.vn.groupbase.common.ReloadListener;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.model.AboutModel;
import dev.vn.groupbase.model.callback.ModelCallBackAbout;
import gmo.hcm.net.lib.RequestError;


public class MainActivity extends ActivityCommon implements ModelCallBackAbout ,ReloadListener{
    private AboutModel mModel;
    private int status = 9;
    private YouTubeEntity dataYouTubeEntity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setLayOutCommon(R.layout.activity_main);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        MobileAds.initialize(getApplicationContext(),getString(R.string.ad_admod_init));
        setReloadListener(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        if (BuildConfig.DEBUG) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("73C26E9772F06EC5DBD172AFB699FFC8").build();
            mAdView.loadAd(adRequest);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        mModel = new AboutModel(this);
        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataYouTubeEntity == null) {
                    mModel.checkApp();
                    status = 8;
                } else {
                    if (dataYouTubeEntity.status == 0) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.channel_address))));
                        ViewManager.getInstance().closeApplication();
                    } else if (dataYouTubeEntity.status == 1) {
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        finish();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle(getString(R.string.dialog_title));
                        dialog.setMessage(getString(R.string.dialog_message));
                        dialog.setCancelable(false);
                        dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                ViewManager.getInstance().closeApplication();
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }
                }
            }
        });
        TextView infor = (TextView) findViewById(R.id.tv_info);
        infor.setText(String.format(getString(R.string.infor), getString(R.string.app_name)));
        mModel.checkApp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onData(YouTubeEntity data) {
        dataYouTubeEntity = data;
        if (data.status == 0) {
            if (status != 9) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.channel_address))));
                ViewManager.getInstance().closeApplication();
            }
        } else if (data.status == 1) {
            if (status != 9) {
                startActivity(new Intent(this, SplashActivity.class));
                finish();
            }
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.dialog_title));
            dialog.setMessage(getString(R.string.dialog_message));
            dialog.setCancelable(false);
            dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ViewManager.getInstance().closeApplication();
                }
            });
            dialog.create();
            dialog.show();
        }
        status = data.status;
    }

    @Override
    public void onError(RequestError error_type) {
        status = 9;
        switch (error_type) {
            case NETWORK:
            case NETWORK_LOST:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("error");
                dialog.setMessage(getString(R.string.error));
                dialog.setCancelable(false);
                dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
                dialog.create();
                dialog.show();
                break;
        }
    }

    @Override
    public void onReload() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("error");
        dialog.setMessage(getString(R.string.error));
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    public void onShowError() {

    }

    @Override
    public void onHideError() {

    }
}
