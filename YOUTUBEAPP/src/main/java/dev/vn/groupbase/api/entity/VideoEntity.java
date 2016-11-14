package dev.vn.groupbase.api.entity;

/**
 * Created by nghiath on 11/10/16.
 */

public class VideoEntity extends YoutubeResponseBase{
    public Snippet snippet;
    public Status status;
    public static class Snippet {
        public String title;
        public String description;
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
    public static class Status {
        public String privacyStatus;
    }
}
