package com.xiaoli.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;


/**
 *Sqlite帮助类，用他获取db
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/xiaoli/db/";// 数据库存储位置
    private static final String DB_NAME = "xiaoli.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public SqliteHelper(Context context) {
        super(context, DB_PATH + DB_NAME, null, version);
    }

    public SqliteHelper(Context context,String dbPath,String dbName){
        super(context, dbPath + dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists  test(" + "id integer primary key autoincrement," + "userId text ," + "carName text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table if exists test");
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
