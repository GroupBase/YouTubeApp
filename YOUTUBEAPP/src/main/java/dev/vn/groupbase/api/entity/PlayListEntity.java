package dev.vn.groupbase.api.entity;

/**
 * Created by acnovn on 10/14/16.
 */

public class PlayListEntity extends YoutubeResponseBase{
    public Snippet snippet;
    public Status status;
    public ContentDetails contentDetails;
    public static class Snippet {
        public String title;
        public String description;
        public String publishedAt;
        public String channelTitle;
        public String channelId;
        public Thumbnails thumbnails;
        public static class Thumbnails {
            public ImageUrl default_url;
            public ImageUrl medium;
            public ImageUrl high;
            public ImageUrl standard;
            public ImageUrl maxres;

            public static class ImageUrl extends dev.vn.groupbase.api.entity.ImageUrl{
                public int width;
                public int height;
            }
        }
    }

    public static class Status {
        public String privacyStatus;
    }
    public static class ContentDetails {
        public int itemCount;
    }
}
