package dev.vn.groupbase.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.activity.BaseActivity;

/**
 * Created by acnovn on 10/14/16.
 */

public abstract class FragmentCommon extends Fragment {
    protected ModelCommon mModelCommon;
    private View mView;
    private boolean isFirst = false;
    protected Context mContext;
    protected ViewGroup mToolBarView;
    protected View mToolbarLeft;
    protected boolean mShowToolBar = true;

    public FragmentCommon() {
        mModelCommon = initModel();
        if (mModelCommon == null) {
            mModelCommon = new ModelCommon();
        }
    }

    public static <T extends FragmentCommon> T newInstance(Bundle data, Class<T> fragment) {
        FragmentCommon result = null;
        try {
            result = fragment.newInstance();
            if (data != null) {
                result.setArguments(data);
            }
            return fragment.cast(result);
        } catch (java.lang.InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DebugLog.log(this.getClass().getSimpleName(), "onAttach");
        mContext = getActivity();
        if (mModelCommon != null) {
            mModelCommon.setmContext(getActivity());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DebugLog.log(this.getClass().getSimpleName(), "onCreate");
        isFirst = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DebugLog.log(this.getClass().getSimpleName(), "onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DebugLog.log(this.getClass().getSimpleName(), "onCreateView");
        this.mView = inflater.inflate(getLayoutId(), container, false);
        return this.mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DebugLog.log(this.getClass().getSimpleName(), "onViewCreated");
        // get data transfer
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mModelCommon.getData(bundle);
        }
        initView();
        // get data API
        if (isFirst) {
            mModelCommon.onStart();
            isFirst = false;
        } else {
            mModelCommon.onResume();
        }
        if (mShowToolBar) {
            if (getActivity() instanceof BaseActivity) {
                ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.show();
                }
                createToolBar();
            }

        } else {
            if (getActivity() instanceof BaseActivity) {
                ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    protected void createToolBar() {
        mToolBarView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.base_menu_top,
                null);
        /*View layoutToolbar = getActivity().getLayoutInflater().inflate(R.layout.base_menu_top,
                null);
        mToolBarView = (ViewGroup) layoutToolbar;
        mToolbarLeft = LayoutInflater.from(mContext).inflate(
                getToolbarLeftResId(), null);
        mToolbarCenter = LayoutInflater.from(mContext).inflate(
                getToolbarCenterResId(), null);
        mToolbarRight = LayoutInflater.from(mContext).inflate(
                getToolbarRightResId(), null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mToolBarView.addView(mToolbarLeft, layoutParams);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        mToolBarView.addView(mToolbarRight, layoutParams);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mToolBarView.addView(mToolbarCenter, layoutParams);*/
        mToolbarLeft = ((BaseActivity) getActivity()).getToolbar().findViewById(R.id.mn_left);
//        mToolbarCenter = ((BaseActivity) getActivity()).getToolbar().findViewById(R.id.mn_search);
        setVisibilityActionBar();
        handleToolBar();
//        Toolbar toolbar = ((BaseActivity) getActivity()).getToolbar();
//        if (toolbar != null) {
//            toolbar.removeAllViews();
//            toolbar.addView(mToolBarView);
//            ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
//        }
    }


    public void setVisibilityActionBar() {
        mToolbarLeft.setVisibility(View.GONE);
    }

    public void handleToolBar() {
        mToolbarLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPress();
                return false;
            }
        });
    }

    public void hideToolBar() {
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void showToolBar() {
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    // implement back press
    public void onBackPress() {
        int count = ViewManager.getInstance().getFragmentManager()
                .getBackStackEntryCount();
        if (count > 1) {
            ViewManager.getInstance().backPreviousFragment();
        } else {
            // back Activity;
            ViewManager.getInstance().getActivity().finish();
        }
    }

    public View findViewById(int id) {
        return mView.findViewById(id);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract ModelCommon initModel();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        DebugLog.log(this.getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        DebugLog.log(this.getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DebugLog.log(this.getClass().getSimpleName(), "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugLog.log(this.getClass().getSimpleName(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DebugLog.log(this.getClass().getSimpleName(), "onDetach");
    }
}
