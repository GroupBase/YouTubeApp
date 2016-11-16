package dev.vn.groupbase;

import android.content.Context;
import android.content.SharedPreferences;

import dev.vn.groupbase.common.ViewManager;


/**
 * Created by TruongHieuNghia on 8/29/15.
 */
public class PreferenceManager {
    private final static String PREF_NAME = "youyubeapp";
    private SharedPreferences mPreferences;
    private PreferenceManager(Context context) {
        mPreferences = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceManager newInstance() {
        return new PreferenceManager(ViewManager.getInstance().getActivity());
    }
    public void setShowAbout(boolean isAbout){
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean("isAbout",isAbout);
        editor.commit();
    }
    public boolean isShowAbout(){
        return mPreferences.getBoolean("isAbout",false);
    }

}
