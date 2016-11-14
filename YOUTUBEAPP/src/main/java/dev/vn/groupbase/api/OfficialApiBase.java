package dev.vn.groupbase.api;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dev.vn.groupbase.DeveloperKey;
import gmo.hcm.net.lib.ApiJsonBase;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 10/19/16.
 */

public abstract class OfficialApiBase extends ApiJsonBase {
    protected Map<String, String> mParams = new HashMap<>();

    public OfficialApiBase(ApiListener apiListener) {
        super(apiListener);
        mParams.put("key", DeveloperKey.DEVELOPER_KEY_YOUTUBE);
        mParams.put("maxResults","50");
    }

    public OfficialApiBase() {
        mParams.put("key", DeveloperKey.DEVELOPER_KEY_YOUTUBE);
    }

    @Override
    public void execute(String url, Map<String, String> params, int method) {

    }

    @Override
    public void execute(String url, Map<String, String> params, JSONObject jsonBody, int method) {

    }

    public void setNextPage(String nextPageToken) {
        mParams.put("pageToken", nextPageToken);
    }

    public void setPrevPage(String prevPageToken) {
        mParams.put("pageToken", prevPageToken);
    }

    public abstract String getUrl();
    public abstract void execute();
    public void setMaxResults(String maxResults){
        mParams.put("maxResults",maxResults);
    }
}
