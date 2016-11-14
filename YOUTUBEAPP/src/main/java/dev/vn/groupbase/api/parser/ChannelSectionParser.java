package dev.vn.groupbase.api.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.vn.groupbase.api.entity.ChannelSectionEntity;
import dev.vn.groupbase.common.DebugLog;

/**
 * Created by acnovn on 10/19/16.
 */

public class ChannelSectionParser extends ParserYoutubeBase {
    public static ArrayList<ChannelSectionEntity> parser(String jsonResult) {
        ArrayList<ChannelSectionEntity> list = new ArrayList<>();
        ChannelSectionParser parser = new ChannelSectionParser();
        try {
            JSONObject root = new JSONObject(jsonResult);
            JSONArray items = getItems(root);
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    ChannelSectionEntity obj = new ChannelSectionEntity();
                    JSONObject item_child = items.getJSONObject(i);
                    obj.kind = getKind(item_child);
                    obj.etag = getEtag(item_child);
                    obj.id = getId(item_child);
                    JSONObject json_snippet = getSnippet(item_child);
                    obj.snippet = parser.parserSnippet(json_snippet, ChannelSectionEntity.Snippet.class);
                    JSONObject json_contentDetail = getContentDetails(item_child);
                    if (json_contentDetail != null) {
                        obj.contentDetails = parser.parserContentDetails(json_contentDetail, ChannelSectionEntity.ContentDetails.class);
                    }
                    list.add(obj);
                }
            }
        } catch (JSONException e) {
            DebugLog.log_e("JsonParse_ChannelSectionParser", e.getMessage());
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    protected <T> T parserSnippet(JSONObject json_snippet, Class<T> object) throws JSONException {
        if (json_snippet != null) {
            ChannelSectionEntity.Snippet snippet = new ChannelSectionEntity.Snippet();
            snippet.type = getValue(json_snippet,"type",String.class);
            snippet.channelId = getValue(json_snippet,"channelId",String.class);
            snippet.title = getValue(json_snippet,"title",String.class);
            snippet.position = getValue(json_snippet,"position",Integer.class);
            return object.cast(snippet);
        }
        return null;
    }

    @Override
    protected <T> T parserStatus(JSONObject jsonObject, Class<T> object) throws JSONException {
        return null;
    }

    @Override
    protected <T> T parserContentDetails(JSONObject json_contentDetail, Class<T> object) throws JSONException {
        if (json_contentDetail != null) {
            ChannelSectionEntity.ContentDetails contentDetails = new ChannelSectionEntity.ContentDetails();
            if (json_contentDetail.has("playlists")) {
                JSONArray playLists = json_contentDetail.getJSONArray("playlists");
                for (int j = 0; j < playLists.length(); j++) {
                    contentDetails.playlists.add(playLists.getString(j));
                }
            }
            if (json_contentDetail.has("channels")) {
                JSONArray channels = json_contentDetail.getJSONArray("channels");
                for (int k = 0; k < channels.length(); k++) {
                    contentDetails.channels.add(channels.getString(k));
                }
            }
            return object.cast(contentDetails);
        }
        return null;
    }

    @Override
    protected <T> T parserThumbnails(JSONObject jsonObject, Class<T> object) throws JSONException {
        return null;
    }
}
