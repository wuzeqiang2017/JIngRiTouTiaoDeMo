package test.bwie.com.wzq_0831jinritoutiao.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.wzq_0831jinritoutiao.bean.User;
import test.bwie.com.wzq_0831jinritoutiao.db.MySqliteOpenHelper;

/**
 * 移动1507D  武泽强
 * 2017/9/6.
 * 作用：
 */

public class SqliteDao {

    private final MySqliteOpenHelper mySqliteOpenHelper;

    public SqliteDao(Context context) {

        mySqliteOpenHelper = new MySqliteOpenHelper(context);

    }
    //t添加数据库
    public void add1(String title, String context, String type){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("context",context);
        values.put("type" ,type);
        db.insert("data",null,values);
        db.close();
    }
    //缓存的数据
    public void addcache(String title,String js){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("js",js);
        db.insert("cache",null,values);
        db.close();
    }

    //修改数据
    public void up1(String title){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        db.execSQL("update data set type=1 where title=? ",new String[]{title});
        System.out.println("update data set type=1 where title="+title);
        db.close();

    }
    //修改数据
    public void up2(String title){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        db.execSQL("update data set type=2 where title=? ",new String[]{title});
        db.close();

    }

    //查询
    public List<User> Select1(){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        List<User> list = new ArrayList<User>();
        Cursor cursor = db.rawQuery("select * from data where type=1",null);
        while(cursor.moveToNext()){
            String title1 = cursor.getString(1);
            String context1 = cursor.getString(2);
            list.add(new User(title1,context1));
        }
        return list;
    }


    //查询  type2
    public List<User> Select2(){
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        List<User> list = new ArrayList<User>();
        Cursor cursor = db.rawQuery("select * from data where type=2",null);
        while(cursor.moveToNext()){
            String title1 = cursor.getString(1);
            String context1 = cursor.getString(2);
            list.add(new User(title1,context1));
        }
        return list;

    }

}
