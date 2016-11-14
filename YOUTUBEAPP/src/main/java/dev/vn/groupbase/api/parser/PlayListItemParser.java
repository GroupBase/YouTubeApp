package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 10/24/16.
 */

public class PlayListItemParser extends ParserYoutubeBase {
    public static ArrayList<PlayListItemEntity> parser(String jsonResult) {
        ArrayList<PlayListItemEntity> lst = new ArrayList<>();
        PlayListItemParser mParser = new PlayListItemParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item_child = items.getJSONObject(i);
                    PlayListItemEntity obj = new PlayListItemEntity();
                    obj.etag = getEtag(item_child);
                    obj.id = getId(item_child);
                    obj.kind = getKind(item_child);
                    obj.snippet = mParser.parserSnippet(getSnippet(item_child), PlayListItemEntity.Snippet.class);
                    obj.status = mParser.parserStatus(getStatus(item_child), PlayListItemEntity.Status.class);
                    obj.contentDetails = mParser.parserContentDetails(getContentDetails(item_child), PlayListItemEntity.ContentDetails.class);
                    if (obj.status.privacyStatus.equalsIgnoreCase("public")) {
                        lst.add(obj);
                    }
                }
            }
        } catch (JSONException e) {
            DebugLog.log_e("JsonParse_PlayListItemParser", e.getMessage());
            return new ArrayList<>();
        }

        return lst;

    }

    @Override
    protected <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException {
        if (json_snippet != null) {
            PlayListItemParser parser = new PlayListItemParser();
            PlayListItemEntity.Snippet snippet = new PlayListItemEntity.Snippet();
            snippet.channelId = getValue(json_snippet, "channelId", String.class);
            snippet.channelTitle = getValue(json_snippet, "channelTitle", String.class);
            snippet.description = getValue(json_snippet, "description", String.class);
            snippet.publishedAt = getValue(json_snippet, "publishedAt", String.class);
            snippet.title = getValue(json_snippet, "title", String.class);
            snippet.playlistId = getValue(json_snippet, "playlistId", String.class);
            snippet.position = getValue(json_snippet, "position", Integer.class);
            snippet.thumbnails = parser.parserThumbnails(getThumbnails(json_snippet), PlayListItemEntity.Snippet.Thumbnails.class);
            return object.cast(snippet);
        }
        return null;
    }

    @Override
    protected <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException {
        if (status_json != null) {
            PlayListItemEntity.Status status = new PlayListItemEntity.Status();
            status.privacyStatus = getValue(status_json, "privacyStatus", String.class);
            return object.cast(status);
        }
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException {
        if (json_contentDetail != null) {
            PlayListItemEntity.ContentDetails contentDetails = new PlayListItemEntity.ContentDetails();
            contentDetails.videoId = getValue(json_contentDetail, "videoId", String.class);
            return object.cast(contentDetails);
        }
        return object.cast(new PlayListItemEntity.ContentDetails());
    }

    @Override
    protected <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException {
        if (thumbnails_json != null) {
            PlayListItemEntity.Snippet.Thumbnails thumbnails = new PlayListItemEntity.Snippet.Thumbnails();
            thumbnails.default_url = thumbnails_json.has("default") == true ? parserImage(thumbnails_json.getJSONObject("default")) : new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.high = thumbnails_json.has("high") == true ? parserImage(thumbnails_json.getJSONObject("high")) : new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.medium = thumbnails_json.has("medium") == true ? parserImage(thumbnails_json.getJSONObject("medium")) : new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.maxres = thumbnails_json.has("maxres") == true ? parserImage(thumbnails_json.getJSONObject("maxres")) : new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.standard = thumbnails_json.has("standard") == true ? parserImage(thumbnails_json.getJSONObject("standard")) : new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
            return object.cast(thumbnails);
        }
        return object.cast(new PlayListItemEntity.Snippet.Thumbnails());
    }

    private static PlayListItemEntity.Snippet.Thumbnails.ImageUrl parserImage(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            PlayListItemEntity.Snippet.Thumbnails.ImageUrl imageUrl = new PlayListItemEntity.Snippet.Thumbnails.ImageUrl();
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
        return null;
    }
}
