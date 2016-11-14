package dev.vn.groupbase.api.entity;

/**
 * Created by acnovn on 11/3/16.
 */

public class SearchEnity {
    public String id;
    public Snippet snippet;
    public static class Snippet {
        public String title;
        public Thumbnails thumbnails;

        public static class Thumbnails {
            public ImageUrl default_url;
            public ImageUrl medium;
            public ImageUrl high;

            public static class ImageUrl extends dev.vn.groupbase.api.entity.ImageUrl {
                public int width;
                public int height;
            }
        }
    }
}
