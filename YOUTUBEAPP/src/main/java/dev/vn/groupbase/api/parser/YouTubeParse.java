package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.YouTubeEntity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by nghiath on 11/18/16.
 */

public class YouTubeParse {
    public static ArrayList<YouTubeEntity> parser(String jsonResult) {
        ArrayList<YouTubeEntity> lst = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResult);
            if (root.has("list_app")){
                JSONArray lis_app_json = root.getJSONArray("list_app");
                for (int i =0 ; i < lis_app_json.length(); i++){
                    YouTubeEntity obj = new YouTubeEntity();
                    JSONObject object = lis_app_json.getJSONObject(i);
                    if (object.has("status")){
                        obj.status = object.getInt("status");
                    }
                    if (object.has("packageId")){
                        obj.packageId = object.getString("packageId");
                    }
                    if (object.has("appName")){
                        obj.appName = object.getString("appName");
                    }
                    if (object.has("ad_start_app")){
                        obj.ad_start_app = object.getString("ad_start_app");
                    }
                    if (object.has("ad_admod_init")){
                        obj.ad_admod_init = object.getString("ad_admod_init");
                    }
                    if (object.has("ad_admod_key")){
                        obj.ad_admod_key = object.getString("ad_admod_key");
                    }
                    if (object.has("version_new")){
                        obj.version_new = object.getInt("version_new");
                    }
                    if (object.has("version_old")){
                        obj.version_old = object.getInt("version_old");
                    }
                    if (object.has("versionName")){
                        obj.versionName = object.getString("version_old");
                    }
                    lst.add(obj);
                }
            }
        } catch (JSONException e) {
            DebugLog.log_e("JsonParse_YouTubeParse", e.getMessage());
            return new ArrayList<>();
        }
        return lst;
    }
}
