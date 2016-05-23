package com.icephone.sw.androidweekly.db;


import android.content.ContentProviderClient;
import android.content.Context;
import android.util.Pair;

import com.icephone.sw.androidweekly.AndroidWeeklyApplication;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sw on 2016/5/22.
 */
public class BatchSQLExecutor {
    protected ConcurrentLinkedQueue<Pair<String,Object[]>> sqls =
            new ConcurrentLinkedQueue<Pair<String, Object[]>>();

    private Iterator<Pair<String,Object[]>> iterator;

    public BatchSQLExecutor(Context context){

    }

    public void appendSQL(String sql,Object[] bindArgs){
        sqls.add(new Pair<String, Object[]>(sql,bindArgs));
    }

    public boolean execute(){
        boolean ret = false;
        ContentProviderClient cc = null;
        try {
            cc = AndroidWeeklyApplication.getContext().getContentResolver()
                    .acquireContentProviderClient(DatabaseTable.AUTHORITY);
            AndroidWeeklyProvider c = (AndroidWeeklyProvider)cc.getLocalContentProvider();

            ret = c.execSQL(this);

            if(ret){
                sqls.clear();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }finally {
            if(cc != null){
                cc.release();
            }
        }
        return ret;
    }

    public void begin(){
        iterator = sqls.iterator();
    }

    public Pair<String,Object[]> next(){
        if(iterator == null){
            return null;
        }
        if(iterator.hasNext()){
            return iterator.next();
        }
        return null;
    }

    public void end(){
        iterator = null;
    }

}
