package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.SearchEnity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 11/3/16.
 */

public class SearchParser extends ParserYoutubeBase {
    public static ArrayList<SearchEnity> parser(String jsonResult) {
        ArrayList<SearchEnity> lst = new ArrayList<>();
        SearchParser mParser = new SearchParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item_child = items.getJSONObject(i);
                    SearchEnity obj = new SearchEnity();
                    if (item_child.has("id")){
                        JSONObject id = item_child.getJSONObject("id");
                        obj.id = id.getString("videoId");
                    }
                    obj.snippet = mParser.parserSnippet(getSnippet(item_child),SearchEnity.Snippet.class);
                    lst.add(obj);
                }
            }
        } catch (JSONException e) {
            DebugLog.log_e("JsonParse", e.getMessage());
            return new ArrayList<>();
        }
        return lst;
    }
    @Override
    protected <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException {
        if (json_snippet != null) {
            SearchParser parser = new SearchParser();
            SearchEnity.Snippet snippet = new SearchEnity.Snippet();
            snippet.title = json_snippet.getString("title");
            snippet.thumbnails = parser.parserThumbnails(getThumbnails(json_snippet),SearchEnity.Snippet.Thumbnails.class);
            return object.cast(snippet);
        }
        return object.cast(new SearchEnity.Snippet());
    }

    @Override
    protected <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException {
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException {
        return null;
    }

    @Override
    protected <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException {
        if (thumbnails_json != null) {
            SearchEnity.Snippet.Thumbnails thumbnails = new SearchEnity.Snippet.Thumbnails();
            thumbnails.default_url = thumbnails_json.has("default") == true ? parserImage(thumbnails_json.getJSONObject("default")) : new SearchEnity.Snippet.Thumbnails.ImageUrl();
            thumbnails.high = thumbnails_json.has("high") == true ? parserImage(thumbnails_json.getJSONObject("high")) : new SearchEnity.Snippet.Thumbnails.ImageUrl();
            thumbnails.medium = thumbnails_json.has("medium") == true ? parserImage(thumbnails_json.getJSONObject("medium")) : new SearchEnity.Snippet.Thumbnails.ImageUrl();
            return object.cast(thumbnails);
        }
        return object.cast(new SearchEnity.Snippet.Thumbnails());
    }
    private static SearchEnity.Snippet.Thumbnails.ImageUrl parserImage(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            SearchEnity.Snippet.Thumbnails.ImageUrl imageUrl = new SearchEnity.Snippet.Thumbnails.ImageUrl();
            if (jsonObject.has("url")) {
                imageUrl.url = jsonObject.getString("url");
            }
            if (jsonObject.has("width")) {
                imageUrl.width = jsonObject.getInt("width");
            }
            if (jsonObject.has("url")) {
                imageUrl.height = jsonObject.getInt("height");
            }
            return imageUrl;
        }
        return new SearchEnity.Snippet.Thumbnails.ImageUrl();
    }
}
