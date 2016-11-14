package dev.vn.groupbase.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import dev.vn.groupbase.App;
import dev.vn.groupbase.api.entity.ChannelSectionEntity;
import dev.vn.groupbase.common.DebugLog;
import gmo.hcm.net.lib.SQLiteDB;

/**
 * Created by acnovn on 11/7/16.
 */

public class YouTubeAppManager {

    public static boolean insertChannelSectionList(ArrayList<ChannelSectionEntity> lst) {
        SQLiteDB db = App.getInstance().db;
        db.delete("channel_section_table", null, null);
        for (ChannelSectionEntity obj : lst) {
            ChannelSectionsTable table = new ChannelSectionsTable();
            table.id = obj.id;
            table.name = obj.snippet.title;
            table.playList = obj.contentDetails.playlists.toString();
            if (db.insert("channel_section_table", null, table.getContentValues()) == -1) {
                DebugLog.log_e("insert db", "Not OK");
                return false;
            } else {
                DebugLog.log_e("insert db", " OK");
            }

        }
        return true;
    }

    public static ArrayList<ChannelSectionsTable> getChannelSectionList() {
        SQLiteDB db = App.getInstance().db;
        ArrayList<ChannelSectionsTable> lst = new ArrayList<>();
        String sql = "select id, name, playList FROM channel_section_table ";
        Cursor c = db.query(sql, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            do {
                ChannelSectionsTable obj = new ChannelSectionsTable();
                obj.id = c.getString(c.getColumnIndex("id"));
                obj.name = c.getString(c.getColumnIndex("name"));
                obj.playList = c.getString(c.getColumnIndex("playList"));
                lst.add(obj);
            } while (c.moveToNext());
            c.close();
        } else {
            c.close();
        }
        return lst;
    }

    //
    public static boolean insertHistory(HistoryTable historyTable) {
        SQLiteDB db = App.getInstance().db;
        String sql = "select videoId,playListId,videoName,playListName,imgVideo,imgPlayList from history_table";
        sql += " where videoId = ? and playListId = ?";
        Cursor c = db.query(sql, new String[]{historyTable.videoId, historyTable.playListId});
        if (c.getCount() > 0) {
            //up date
            db.delete("history_table", "videoId=? and playListId=?", new String[]{historyTable.videoId, historyTable.playListId});
            DebugLog.log_e("insert db", "Not OK");
        }
        if (db.insert("history_table", null, historyTable.getContentValues()) == -1) {
            DebugLog.log_e("insert db", "Not OK");
            return false;
        }
        DebugLog.log_e("insert db", " OK");
        return true;
    }

    public static List<HistoryTable> getHistory() {
        SQLiteDB db = App.getInstance().db;
        List<HistoryTable> lst = new ArrayList<>();
        String sql = "select videoId,playListId,videoName,playListName,imgVideo,imgPlayList from history_table";
        Cursor c = db.query(sql, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            do {
                HistoryTable obj = new HistoryTable();
                obj.videoId = c.getString(c.getColumnIndex("videoId"));
                obj.playListId = c.getString(c.getColumnIndex("playListId"));
                obj.videoName = c.getString(c.getColumnIndex("videoName"));
                obj.playListName = c.getString(c.getColumnIndex("playListName"));
                obj.imgVideo = c.getString(c.getColumnIndex("imgVideo"));
                obj.imgPlayList = c.getString(c.getColumnIndex("imgPlayList"));
                lst.add(obj);
            } while (c.moveToNext());
            c.close();
        } else {
            c.close();
        }
        return lst;
    }

    //book mark
    public static boolean insertBookMark(BookMarkTable bookMarkTable) {
        SQLiteDB db = App.getInstance().db;
        if (db.insert("book_mark_table", null, bookMarkTable.getContentValues()) == -1) {
            DebugLog.log_e("insert db", "Not OK");
            return false;
        }
        DebugLog.log_e("insert db", " OK");
        return true;
    }

    public static List<BookMarkTable> getBookMark() {
        SQLiteDB db = App.getInstance().db;
        List<BookMarkTable> lst = new ArrayList<>();
        String sql = "select videoId,playListId,videoName,playListName,imgVideo,imgPlayList from book_mark_table";
        Cursor c = db.query(sql, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            do {
                BookMarkTable obj = new BookMarkTable();
                obj.videoId = c.getString(c.getColumnIndex("videoId"));
                obj.playListId = c.getString(c.getColumnIndex("playListId"));
                obj.videoName = c.getString(c.getColumnIndex("videoName"));
                obj.playListName = c.getString(c.getColumnIndex("playListName"));
                obj.imgVideo = c.getString(c.getColumnIndex("imgVideo"));
                obj.imgPlayList = c.getString(c.getColumnIndex("imgPlayList"));
                lst.add(obj);
            } while (c.moveToNext());
            c.close();
        } else {
            c.close();
        }
        return lst;
    }

    public static boolean checkExitsBookMark(String videoId, String playListId) {
        boolean isExits = false;
        SQLiteDB db = App.getInstance().db;

        String sql = "select * From book_mark_table where videoId=? and playListId=?";
        Cursor c = db.query(sql, new String[]{videoId, playListId});
        if (c.getCount() > 0) {
            isExits = true;
        }
        c.close();
        return isExits;
    }
}
