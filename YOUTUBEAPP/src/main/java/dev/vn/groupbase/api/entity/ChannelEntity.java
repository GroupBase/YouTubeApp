package dev.vn.groupbase.api.entity;

public class ChannelEntity extends YoutubeResponseBase{
    public Snippet snippet;
    public Status status;

    public static class Snippet {
        public String title;
        public String description;
        public String publishedAt;
        public Thumbnails thumbnails;

        public static class Thumbnails {
            public ImageUrl default_url;
            public ImageUrl medium;
            public ImageUrl high;
        }
    }

    public static class Status {
        public String privacyStatus;
        public boolean isLinked;
        public String longUploadsStatus;

    }
}