package dev.vn.groupbase.api.entity;

/**
 * Created by acnovn on 10/24/16.
 */

public class PlayListItemEntity extends YoutubeResponseBase {
    public Snippet snippet;
    public ContentDetails contentDetails;
    public Status status;
    public static class Snippet {
        public String title;
        public String description;
        public String publishedAt;
        public String channelTitle;
        public String channelId;
        public String playlistId;
        public int position;
        public Thumbnails thumbnails;

        public static class Thumbnails {
            public ImageUrl default_url;
            public ImageUrl medium;
            public ImageUrl high;
            public ImageUrl standard;
            public ImageUrl maxres;

            public static class ImageUrl extends dev.vn.groupbase.api.entity.ImageUrl {
                public int width;
                public int height;
            }
        }
    }
    public static class ContentDetails {
        public String videoId;
    }
    public static class Status {
        public String privacyStatus;
    }
}
