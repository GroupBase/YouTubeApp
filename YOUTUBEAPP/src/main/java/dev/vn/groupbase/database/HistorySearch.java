package dev.vn.groupbase.database;

import android.content.ContentValues;

/**
 * Created by nghiath on 11/10/16.
 */

public class HistorySearch {
    public int id;
    public String keyWord;
    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("id",this.id);
        values.put("keyWord",this.keyWord);
        return values;
    }
}
