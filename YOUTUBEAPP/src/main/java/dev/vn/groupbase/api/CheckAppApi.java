package dev.vn.groupbase.api;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.thn.groupbase.phimkinhdi.BuildConfig;
import gmo.hcm.net.lib.ApiJsonBase;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by nghiath on 11/18/16.
 */

public class CheckAppApi extends ApiJsonBase {
    private Map<String, String> mParams = new HashMap<>();
    public CheckAppApi (ApiListener apiListener){
        super(apiListener);
    }
    @Override
    public void execute(String url, Map<String, String> params, int method) {

    }

    @Override
    public void execute(String url, Map<String, String> params, JSONObject jsonBody, int method) {

    }
    public void execute() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username","truonghieunghia_acnovn");
            jsonObject.put("pass","abc201114abc251184");
            if (BuildConfig.DEBUG) {
                jsonObject.put("app_build", "2");
            } else {
                jsonObject.put("app_build", "1");
            }
        } catch (JSONException e) {
            jsonObject = new JSONObject();
        }
        Map<String,String> params_header = new HashMap<String, String>();
        params_header.put("Content-Type","application/x-www-form-urlencoded");
        setHeader(params_header);
        this.asyncRequest(YoutubeConstant.CHECK_APP,jsonObject, null, Request.Method.POST);
    }

}
