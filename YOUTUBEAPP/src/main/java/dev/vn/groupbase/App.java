package dev.vn.groupbase;


import android.content.pm.PackageManager;

import gmo.hcm.net.lib.OpenSQLiteBase;
import gmo.hcm.net.lib.SQLiteDB;
import gmo.hcm.net.lib.SQLiteManager;
import gmo.hcm.net.lib.ApplicationBase;

/**
 * Created by acnovn on 10/14/16.
 */

public class App extends ApplicationBase {
    @Override
    public void onCreate() {
        super.onCreate();
        PackageManager manager = this.getPackageManager();

        OpenSQLiteBase openSQLiteBase = new OpenSQLiteBase(this,"official.sqlite",1,true,this.getDatabasePath("official.sqlite").getPath());
        SQLiteManager.initializeInstance(openSQLiteBase);
        this.sqLiteManager = SQLiteManager.getInstance();
        this.db = sqLiteManager.openDatabase();
    }
}
