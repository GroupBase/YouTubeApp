package dev.vn.groupbase.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.thn.groupbase.youtubeapp.R;


/**
 * Created by acnovn on 10/14/16.
 */

public abstract class ActivityCommon extends AppCompatActivity {
    private int mLayoutCommon = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.log(this.getClass().getSimpleName(),"onCreate");
        if (mLayoutCommon == 0 ) {
            setContentView(R.layout.activity_common);
        } else {
            setContentView(mLayoutCommon);
        }
        ViewManager.getInstance().setActivity(this);
        initView();
        onCreateExecute(savedInstanceState);
    }
    protected void initView(){

    }
    protected void setLayOutCommon(int layoutCommon){
        mLayoutCommon = layoutCommon;
    }
    @Override
    protected void onStart() {
        super.onStart();
        ViewManager.getInstance().startActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewManager.getInstance().setActivity(this);
        ViewManager.getInstance().resumeActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewManager.getInstance().pauseActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewManager.getInstance().stopActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().destroyActivity();
    }

    @Override
    public void onBackPressed() {
        if (ViewManager.getInstance().getCurrentFragment() != null) {
            ViewManager.getInstance().getCurrentFragment().onBackPress();
        } else {
            super.onBackPressed();
        }
    }

    protected abstract void onCreateExecute(Bundle savedInstanceState);
}
