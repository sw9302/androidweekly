package com.icephone.sw.androidweekly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sw on 2016/5/22.
 */
public final class AndroidWeeklyDatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "androidweekly.db";
    private static volatile AndroidWeeklyDatabaseHelper mInstance;

    private AndroidWeeklyDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    public static AndroidWeeklyDatabaseHelper getInstance(Context context){
        if(mInstance == null){
            synchronized (AndroidWeeklyDatabaseHelper.class){
                if(mInstance == null){
                    mInstance = new AndroidWeeklyDatabaseHelper(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();


        sb.append("create table if not exist ");
        sb.append(DatabaseTable.GCTable.TABLE_NAME);
        sb.append(" (");
        sb.append(DatabaseTable.GCTable._ID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(DatabaseTable.GCTable.TYPE).append(" INTEGER,");
        sb.append(DatabaseTable.GCTable.CONTENT).append(" TEXT,");
        sb.append("unique(").append(DatabaseTable.GCTable.TYPE).append(")");

        db.execSQL(sb.toString());

        sb.delete(0,sb.length());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
