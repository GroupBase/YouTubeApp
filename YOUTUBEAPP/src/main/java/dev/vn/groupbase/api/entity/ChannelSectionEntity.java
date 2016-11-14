package dev.vn.groupbase.api.entity;

import java.util.ArrayList;

/**
 * Created by acnovn on 10/19/16.
 */

public class ChannelSectionEntity  extends YoutubeResponseBase{
    public Snippet snippet;
    public ContentDetails contentDetails;
    public static class Snippet {
        public Snippet(){}
        public String type;
        public String channelId;
        public String title;
        public int position;
    }
    public static class ContentDetails {
        public ArrayList<String> playlists = new ArrayList<>();
        public ArrayList<String> channels = new ArrayList<>();
    }
}