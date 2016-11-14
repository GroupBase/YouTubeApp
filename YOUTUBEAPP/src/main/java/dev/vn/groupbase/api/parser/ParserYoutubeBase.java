package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 10/20/16.
 */

public abstract class ParserYoutubeBase {
    public static <T> T getValue(JSONObject jsonObject, String valueName, Class<T> object) throws JSONException {
        if (jsonObject.has(valueName)) {
            return object.cast(jsonObject.get(valueName));
        } else {
            try {
                if (object.newInstance() instanceof String)
                    return object.cast("");
                if (object.newInstance() instanceof Number)
                    return object.cast(0);
                if (object.newInstance() instanceof Boolean)
                    return object.cast(false);
            } catch (InstantiationException e) {
                DebugLog.log_e("JsonParse", e.getMessage());
                return null;
            } catch (IllegalAccessException e) {
                DebugLog.log_e("JsonParse", e.getMessage());
                return null;
            }
        }
        return null;
    }

    public static JSONArray getItems(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("items")) {
            return jsonObject.getJSONArray("items");
        } else {
            return null;
        }
    }

    public static String getKind(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("kind")) {
            return jsonObject.getString("kind");
        } else {
            return "";
        }
    }

    public static String getEtag(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("etag")) {
            return jsonObject.getString("etag");
        } else {
            return "";
        }
    }

    public static String getId(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) {
            return jsonObject.getString("id");
        } else {
            return "";
        }
    }

    public static JSONObject getSnippet(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("snippet")) {
            return jsonObject.getJSONObject("snippet");
        } else {
            return null;
        }
    }

    public static JSONObject getStatus(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("status")) {
            return jsonObject.getJSONObject("status");
        } else {
            return null;
        }
    }

    public static JSONObject getContentDetails(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("contentDetails")) {
            return jsonObject.getJSONObject("contentDetails");
        } else {
            return null;
        }
    }
    public static JSONObject getThumbnails(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("thumbnails")) {
            return jsonObject.getJSONObject("thumbnails");
        } else {
            return null;
        }
    }
    protected abstract <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException;

    protected abstract <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException;

    protected abstract <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException;

    protected abstract <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException;
}
