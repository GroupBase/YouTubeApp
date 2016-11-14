package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.vn.groupbase.api.entity.ChannelEntity;
import dev.vn.groupbase.api.entity.ImageUrl;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 10/20/16.
 */

public class ChannelParser extends ParserYoutubeBase {
    public static ChannelEntity parser(String jsonResult) {
        ChannelEntity channelEntity = new ChannelEntity();
        ChannelParser parser = new ChannelParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                JSONObject item_child = items.getJSONObject(0);
                if (item_child != null) {
                    channelEntity.kind = getKind(item_child);
                    channelEntity.etag = getEtag(item_child);
                    channelEntity.id = getId(item_child);
                    JSONObject snippet_json = getSnippet(item_child);
                    channelEntity.snippet = parser.parserSnippet(snippet_json, ChannelEntity.Snippet.class);
                    JSONObject status_json = getStatus(item_child);
                    channelEntity.status = parser.parserStatus(status_json, ChannelEntity.Status.class);
                    //snippet end
                }
            }

        } catch (JSONException e) {
            DebugLog.log_e("JsonParse", e.getMessage());
            return channelEntity;
        }
        return channelEntity;
    }

    @Override
    protected <T> T parserSnippet(JSONObject snippet_json, Class<T> object) throws JSONException {
        if (snippet_json != null) {
            ChannelParser parser = new ChannelParser();
            ChannelEntity.Snippet snippet = new ChannelEntity.Snippet();
            snippet.title = getValue(snippet_json, "title", String.class);
            snippet.description = getValue(snippet_json, "description", String.class);
            snippet.publishedAt = getValue(snippet_json, "publishedAt", String.class);
            if (snippet_json.has("thumbnails")) {
                JSONObject thumbnails_json = snippet_json.getJSONObject("thumbnails");
                snippet.thumbnails = parser.parserThumbnails(thumbnails_json, ChannelEntity.Snippet.Thumbnails.class);
            }
            return object.cast(snippet);
        }
        return null;
    }

    @Override
    protected <T> T parserStatus(JSONObject status_json, Class<T> object) throws JSONException {
        if (status_json != null) {
            ChannelEntity.Status status = new ChannelEntity.Status();
            status.privacyStatus = getValue(status_json, "privacyStatus", String.class);
            status.isLinked = getValue(status_json, "isLinked", Boolean.class);
            status.longUploadsStatus = getValue(status_json, "longUploadsStatus", String.class);
            return object.cast(status);
        }
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject jsonObject, Class<T> object) throws JSONException {
        return null;
    }

    @Override
    protected <T> T parserThumbnails(JSONObject thumbnails_json, Class<T> object) throws JSONException {
        ChannelEntity.Snippet.Thumbnails thumbnails = new ChannelEntity.Snippet.Thumbnails();
        if (thumbnails_json.has("default")) {
            ImageUrl default_url = new ImageUrl();
            JSONObject default_json = thumbnails_json.getJSONObject("default");
            default_url.url = getValue(default_json, "url", String.class);
            thumbnails.default_url = default_url;
        }
        if (thumbnails_json.has("high")) {
            ImageUrl high = new ImageUrl();
            JSONObject high_json = thumbnails_json.getJSONObject("high");
            high.url = getValue(high_json, "url", String.class);
            thumbnails.high = high;
        }
        if (thumbnails_json.has("medium")) {
            ImageUrl medium = new ImageUrl();
            JSONObject medium_json = thumbnails_json.getJSONObject("medium");
            medium.url = getValue(medium_json, "url", String.class);
            thumbnails.medium = medium;
        }
        return null;
    }
}
