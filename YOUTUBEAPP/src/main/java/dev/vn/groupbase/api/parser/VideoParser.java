package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.VideoEntity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by nghiath on 11/10/16.
 */

public class VideoParser extends ParserYoutubeBase{
    public static ArrayList<VideoEntity> parser(String jsonResult) {
        ArrayList<VideoEntity> lst = new ArrayList<>();
        VideoParser mParser = new VideoParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item_child = items.getJSONObject(i);
                    VideoEntity obj = new VideoEntity();
                    obj.etag = getEtag(item_child);
                    obj.id = getId(item_child);
                    obj.kind = getKind(item_child);
                    obj.snippet = mParser.parserSnippet(getSnippet(item_child), VideoEntity.Snippet.class);
                    obj.status = mParser.parserStatus(getStatus(item_child), VideoEntity.Status.class);
                    if (obj.status.privacyStatus.equalsIgnoreCase("public")) {
                        lst.add(obj);
                    }
                }
            }
        }catch (JSONException e) {
            DebugLog.log_e("JsonParse_PlayListItemParser", e.getMessage());
            return new ArrayList<>();
        }
        return lst;
    }
    @Override
    protected <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException {
        if (json_snippet != null) {
            VideoParser parser = new VideoParser();
            VideoEntity.Snippet snippet = new VideoEntity.Snippet();
            snippet.description = getValue(json_snippet, "description", String.class);
            snippet.title = getValue(json_snippet, "title", String.class);
            snippet.thumbnails = parser.parserThumbnails(getThumbnails(json_snippet), VideoEntity.Snippet.Thumbnails.class);
            return object.cast(snippet);
        }
        return null;
    }

    @Override
    protected <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException {
        if (status_json != null) {
            VideoEntity.Status status = new VideoEntity.Status();
            status.privacyStatus = getValue(status_json, "privacyStatus", String.class);
            return object.cast(status);
        }
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException {
        return null;
    }

    @Override
    protected <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException {
        if (thumbnails_json != null) {
            VideoEntity.Snippet.Thumbnails thumbnails = new VideoEntity.Snippet.Thumbnails();
            thumbnails.default_url = thumbnails_json.has("default") == true ? parserImage(thumbnails_json.getJSONObject("default")) : new VideoEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.high = thumbnails_json.has("high") == true ? parserImage(thumbnails_json.getJSONObject("high")) : new VideoEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.medium = thumbnails_json.has("medium") == true ? parserImage(thumbnails_json.getJSONObject("medium")) : new VideoEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.maxres = thumbnails_json.has("maxres") == true ? parserImage(thumbnails_json.getJSONObject("maxres")) : new VideoEntity.Snippet.Thumbnails.ImageUrl();
            thumbnails.standard = thumbnails_json.has("standard") == true ? parserImage(thumbnails_json.getJSONObject("standard")) : new VideoEntity.Snippet.Thumbnails.ImageUrl();
            return object.cast(thumbnails);
        }
        return object.cast(new VideoEntity.Snippet.Thumbnails());
    }
    private static VideoEntity.Snippet.Thumbnails.ImageUrl parserImage(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            VideoEntity.Snippet.Thumbnails.ImageUrl imageUrl = new VideoEntity.Snippet.Thumbnails.ImageUrl();
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
