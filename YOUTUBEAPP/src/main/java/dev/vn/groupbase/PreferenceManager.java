package dev.vn.groupbase;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by TruongHieuNghia on 8/29/15.
 */
public class PreferenceManager {
    private final static String PREF_NAME = "gameshowtv";
    private SharedPreferences mPreferences;
    private PreferenceManager(Context context) {
        mPreferences = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceManager newInstance(Context context) {
        return new PreferenceManager(context);
    }
    public void setAd_start_app(String ad_start_app){
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("ad_start_app",ad_start_app);
        editor.commit();
    }
    public String getAd_start_app(){
        return mPreferences.getString("ad_start_app","");
    }
    public void setAd_admod_init(String Ad_admod_init){
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("Ad_admod_init",Ad_admod_init);
        editor.commit();
    }
    public String getAd_admod_init(){
        return mPreferences.getString("Ad_admod_init","");
    }
    public void setAd_admod_key(String ad_admod_key){
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("ad_admod_key",ad_admod_key);
        editor.commit();
    }
    public String getAd_admod_key(){
        return mPreferences.getString("ad_admod_key","");
    }
}
