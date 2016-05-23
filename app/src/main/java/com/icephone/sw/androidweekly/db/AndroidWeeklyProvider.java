package com.icephone.sw.androidweekly.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sw on 2016/5/22.
 */
public class AndroidWeeklyProvider extends ContentProvider {

    private AndroidWeeklyDatabaseHelper mDatabaseHelper;

    private static final int URI_GCDATA = 0;
//    private static final int URI_EXEC_SQL = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(DatabaseTable.AUTHORITY, DatabaseTable.GCTable.TABLE_NAME,URI_GCDATA);
//        URI_MATCHER.addURI(DatabaseTable.AUTHORITY,DatabaseTable.SQLExecutor.TABLE_NAME,URI_EXEC_SQL);
    }
    @Override
    public boolean onCreate() {
        mDatabaseHelper = AndroidWeeklyDatabaseHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        final int uriMatch = URI_MATCHER.match(uri);

        switch (uriMatch){
            case URI_GCDATA:{
                cursor = db.query(DatabaseTable.GCTable.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:{
                throw new IllegalArgumentException("Unknow URL " + uri);
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int uriMatch = URI_MATCHER.match(uri);
        long row = -1;
        Uri retUri = null;

        switch (uriMatch){
            case URI_GCDATA:{
                row = db.insert(DatabaseTable.GCTable.TABLE_NAME,
                        null,values);
                retUri = Uri.parse("content://" + DatabaseTable.GCTable.TABLE_NAME + "/" + row);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int uriMatch = URI_MATCHER.match(uri);
        int count = 0;

        switch (uriMatch){
            case URI_GCDATA:{
                count = db.delete(DatabaseTable.GCTable.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URL " + uri);
            }
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int uriMatch = URI_MATCHER.match(uri);

        switch (uriMatch){
            case URI_GCDATA:{
                return db.update(DatabaseTable.GCTable.TABLE_NAME,values,selection,selectionArgs);
            }
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    private Lock _lock_ = new ReentrantLock();

    public boolean execSQL(BatchSQLExecutor executor){
        _lock_.lock();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            executor.begin();
            Pair<String,Object[]> n = null;
            while ((n = executor.next()) != null){
                if(n.second != null){
                    db.execSQL(n.first,n.second);
                }else{
                    db.execSQL(n.first);
                }
            }
            db.setTransactionSuccessful();
            return true;
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }finally {
            executor.end();
            try {
                if(db != null){
                    db.endTransaction();
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
            _lock_.unlock();
        }
    }
}
