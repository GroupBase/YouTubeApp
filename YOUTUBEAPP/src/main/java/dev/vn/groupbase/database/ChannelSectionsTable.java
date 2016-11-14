package dev.vn.groupbase.database;

import android.content.ContentValues;

/**
 * Created by acnovn on 11/7/16.
 */

public class ChannelSectionsTable {
    public String id;
    public String name;
    public String playList;
    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("id",this.id);
        values.put("name",this.name);
        values.put("playList",this.playList);
        return values;
    }
}

