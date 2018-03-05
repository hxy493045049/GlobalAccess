package com.msy.globalaccess.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hxy on 2016/12/6 0006.
 * <p>
 * description :
 */

public class GreenDbHelper extends DaoMaster.OpenHelper {

    public GreenDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.i("lxq", "oldVersion:" + oldVersion + ",newVersion" + newVersion);
        //如果以后需要更新其他表。继续增加参数  XXDao.class
        MigrationHelper.getInstance().migrate(db, UserDao.class);
    }
}
