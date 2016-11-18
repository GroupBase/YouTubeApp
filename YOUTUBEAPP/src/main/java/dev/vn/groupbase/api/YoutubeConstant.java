package dev.vn.groupbase.api;

/**
 * Created by acnovn on 10/19/16.
 */

public class YoutubeConstant {
    private static String DOMAIN = "https://www.googleapis.com/youtube/v3/";
    public static String CHANNEL = String.format("%1schannels", DOMAIN);
    public static String CHANNEL_SECTION = String.format("%1schannelSections", DOMAIN);
    public static String PLAY_LIST = String.format("%1splaylists", DOMAIN);
    public static String PLAY_LIST_ITEMS = String.format("%1splaylistItems", DOMAIN);
    public static String VIDEOS = String.format("%1svideos", DOMAIN);
    public static String SEARCH = String.format("%1ssearch", DOMAIN);
    public static String CHECK_APP = "https://youtubeapp-149902.appspot.com/api/appstore";
}
