package test.bwie.com.wzq_0831jinritoutiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 移动1507D  武泽强
 * 2017/8/31.
 * 作用：
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    public MySqliteOpenHelper(Context context) {
        super(context, "1507Djingritoutiao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table data(id integer primary key autoincrement,title varchar(30),context varchar(30),type varchar(30))");
        db.execSQL("create table cache(id integer primary key autoincrement,title varchar(30),js varchar(50))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
