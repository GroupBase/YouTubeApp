package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 10/14/16.
 */

public class PlayListParser extends ParserYoutubeBase {
    public static ArrayList<PlayListEntity> parser(String jsonResult) {
        ArrayList<PlayListEntity> lst = new ArrayList<>();
        PlayListParser mParser = new PlayListParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item_child = items.getJSONObject(i);
                    PlayListEntity obj = new PlayListEntity();
                    obj.etag = getEtag(item_child);
                    obj.id = getId(item_child);
                    obj.kind = getKind(item_child);
                    obj.snippet = mParser.parserSnippet(getSnippet(item_child), PlayListEntity.Snippet.class);
                    obj.status = mParser.parserStatus(getStatus(item_child), PlayListEntity.Status.class);
                    obj.contentDetails = mParser.parserContentDetails(getContentDetails(item_child), PlayListEntity.ContentDetails.class);
                    if (obj.status.privacyStatus.equalsIgnoreCase("public")) {
                        lst.add(obj);
                    }
                }
            }
        } catch (JSONException e) {
            DebugLog.log_e("JsonParse_PlayListParser", e.getMessage());
            return new ArrayList<>();
        }
        return lst;
    }

    @Override
    protected <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException {
        if (json_snippet != null) {
            PlayListParser parser = new PlayListParser();
            PlayListEntity.Snippet snippet = new PlayListEntity.Snippet();
            snippet.channelId = json_snippet.getString("channelId");
            snippet.channelTitle = json_snippet.getString("channelTitle");
            snippet.description = json_snippet.getString("description");
            snippet.publishedAt = json_snippet.getString("publishedAt");
            snippet.title = json_snippet.getString("title");
            snippet.thumbnails = parser.parserThumbnails(json_snippet.getJSONObject("thumbnails"), PlayListEntity.Snippet.Thumbnails.class);
            return object.cast(snippet);
        }
        return null;
    }

    @Override
    protected <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException {
        if (status_json != null) {
            PlayListEntity.Status status = new PlayListEntity.Status();
            status.privacyStatus = status_json.has("privacyStatus") ? status_json.getString("privacyStatus") : "";
            return object.cast(status);
        }
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException {
        if (json_contentDetail != null){
            PlayListEntity.ContentDetails contentDetails = new PlayListEntity.ContentDetails();
            contentDetails.itemCount = json_contentDetail.has("itemCount")?json_contentDetail.getInt("itemCount"):0;
            return object.cast(contentDetails);
        }
        return object.cast(new PlayListEntity.ContentDetails());
    }

    @Override
    protected <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException {
        if (thumbnails_json != null) {
            PlayListEntity.Snippet.Thumbnails thumbnails = new PlayListEntity.Snippet.Thumbnails();
            thumbnails.default_url = thumbnails_json.has("default") == true ? parserImage(thumbnails_json.getJSONObject("default")) : new PlayListEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.high = thumbnails_json.has("high") == true ? parserImage(thumbnails_json.getJSONObject("high")) : new PlayListEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.medium = thumbnails_json.has("medium") == true ? parserImage(thumbnails_json.getJSONObject("medium")) : new PlayListEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.maxres = thumbnails_json.has("maxres") == true ? parserImage(thumbnails_json.getJSONObject("maxres")) : new PlayListEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.standard = thumbnails_json.has("standard") == true ? parserImage(thumbnails_json.getJSONObject("standard")) : new PlayListEntity.Snippet.Thumbnails.ImageUrl();
            return object.cast(thumbnails);
        }
        return object.cast(new PlayListEntity.Snippet.Thumbnails());
    }

    private static PlayListEntity.Snippet.Thumbnails.ImageUrl parserImage(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            PlayListEntity.Snippet.Thumbnails.ImageUrl imageUrl = new PlayListEntity.Snippet.Thumbnails.ImageUrl();
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
        return new PlayListEntity.Snippet.Thumbnails.ImageUrl();
    }
}
