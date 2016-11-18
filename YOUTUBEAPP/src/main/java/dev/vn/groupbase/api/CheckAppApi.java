package dev.vn.groupbase.api;

import org.json.JSONObject;

import java.util.Map;

import gmo.hcm.net.lib.ApiJsonBase;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by nghiath on 11/18/16.
 */

public class CheckAppApi extends ApiJsonBase {
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
        this.asyncRequest(YoutubeConstant.CHECK_APP, null);
    }

}
