package dev.vn.groupbase.api;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        mParams.put("username","truonghieunghia_acnovn");
        mParams.put("pass","abc201114abc251184");
        Map<String,String> params_header = new HashMap<String, String>();
        params_header.put("Content-Type","application/x-www-form-urlencoded");
        setHeader(params_header);
        this.asyncRequest(YoutubeConstant.CHECK_APP, mParams, Request.Method.POST);
    }

}
