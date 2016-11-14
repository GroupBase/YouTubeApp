package dev.vn.groupbase.database;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by acnovn on 11/7/16.
 */

public class BookMarkTable implements Serializable {
    public String videoId;
    public String playListId;
    public String videoName;
    public String playListName;
    public String imgVideo;
    public String imgPlayList;
    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("videoId",this.videoId);
        values.put("playListId",this.playListId);
        values.put("videoName",this.videoName);
        values.put("playListName",this.playListName);
        values.put("imgVideo",this.imgVideo);
        values.put("imgPlayList",this.imgPlayList);
        return values;
    }
}
