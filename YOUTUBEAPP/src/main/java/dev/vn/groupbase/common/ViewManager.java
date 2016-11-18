package dev.vn.groupbase.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import app.thn.groupbase.gameshowtv.R;


/**
 * Created by acnovn on 10/14/16.
 */

public class ViewManager {
    private static ViewManager mInstance = null;

    private FragmentActivity mActivity;
    private FragmentManager mFragmentManager;

    public synchronized static ViewManager getInstance() {
        if (mInstance == null) {
            mInstance = new ViewManager();
        }
        return mInstance;
    }

    public FragmentActivity getActivity() {
        return mActivity;
    }

    public void setActivity(FragmentActivity activity) {
        this.mActivity = activity;
        setFragmentManager(activity.getSupportFragmentManager());
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

    public void addFragment(FragmentCommon fragment, boolean addStack) {
        if (null != mFragmentManager) {
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment, nameFragment);
            if (addStack) {
                ft.addToBackStack(nameFragment);
            }
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }
    public void addFragment(FragmentCommon fragment,int contentId, boolean addStack) {
        if (null != mFragmentManager) {
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(contentId, fragment, nameFragment);
            if (addStack) {
                ft.addToBackStack(nameFragment);
            }
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }
    public void replaceFragment(FragmentCommon fragment) {
        if (null != mFragmentManager) {
            String nameFragment = fragment.getClass().getName();

            mFragmentManager.popBackStack(nameFragment,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment, nameFragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
        }
    }

    public FragmentCommon getCurrentFragment() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    if (fragment instanceof FragmentCommon) {
                        return (FragmentCommon) fragment;
                    }
            }
        }
        return null;
    }

    public void closeApplication() {;
        mActivity.finishAffinity();
    }

    public void backPreviousFragment() {
        mFragmentManager.popBackStack();
    }

    public void resumeActivity() {
        DebugLog.log(mActivity.getClass().getSimpleName(),"resume");
    }

    public void pauseActivity() {
        DebugLog.log(mActivity.getClass().getSimpleName(),"pause");
    }
    public void destroyActivity() {
        DebugLog.log(mActivity.getClass().getSimpleName(),"Destroy");
    }
    public void stopActivity() {
        DebugLog.log(mActivity.getClass().getSimpleName(),"stop");
    }
    public void startActivity() {
        DebugLog.log(mActivity.getClass().getSimpleName(),"start");
    }
}
