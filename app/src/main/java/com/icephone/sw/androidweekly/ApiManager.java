package com.icephone.sw.androidweekly;

import android.content.Context;
import android.database.Cursor;

import com.icephone.sw.androidweekly.db.BatchSQLExecutor;
import com.icephone.sw.androidweekly.db.DatabaseMap;
import com.icephone.sw.androidweekly.db.DatabaseTable;
import com.icephone.sw.androidweekly.model.WeeklyInfo;
import com.icephone.sw.androidweekly.network.BaseOKHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sw on 2016/5/7.
 */
public class ApiManager extends BaseOKHttpClient<ArrayList<WeeklyInfo>>{
    public ApiManager(Context context,String url) {
        super(context,url);
    }

    @Override
    public JSONObject getFromDatabse() {
        Cursor cursor = null;
        try{
            cursor = mContext.getContentResolver().query(
                    DatabaseTable.GCTable.URI_GCTABLE,
                    DatabaseMap.GCTABLE_PROJECTION,
                    "type=" + DatabaseTable.GCTable.TYPE_WEEKLY,
                    null,null);
            if(cursor != null){
                if (cursor.moveToFirst()){
                    String content = cursor.getString(DatabaseMap.GCTABLE_DATA_INDEX);
                    return new JSONObject(content);
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
        }

        return null;
    }

    @Override
    public void saveToDatabase(JSONObject obj) {
        if(getPageIndex() == 1){
            BatchSQLExecutor sql = new BatchSQLExecutor(mContext);
            sql.appendSQL("insert into " +
                            DatabaseTable.GCTable.TABLE_NAME + " (type,content) values (?,?)",
                    new String[]{DatabaseTable.GCTable.TYPE_WEEKLY + "",obj.toString()});
            sql.execute();
        }
    }

    @Override
    public void prepaerParams() {

    }



    @Override
    public ArrayList<WeeklyInfo> getResult(JSONObject obj) {
        if(obj == null){
            return null;
        }
        JSONArray array = obj.optJSONArray("content");
        if(array == null || array.length() == 0){
            return null;
        }
        ArrayList<WeeklyInfo> ret = new ArrayList<>();
        WeeklyInfo info = null;
        for(int i = 0;i < array.length();i++){
            info = new WeeklyInfo(array.optJSONObject(i));
            ret.add(info);
        }

        return ret;
    }
}
