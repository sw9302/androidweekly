package com.icephone.sw.androidweekly;

import com.icephone.sw.androidweekly.model.WeeklyInfo;
import com.icephone.sw.androidweekly.network.BaseOKHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sw on 2016/5/7.
 */
public class ApiManager extends BaseOKHttpClient<ArrayList<WeeklyInfo>>{
    public ApiManager(String url) {
        super(url);
    }

    @Override
    public void prepaerParams() {

    }


    @Override
    public ArrayList<WeeklyInfo> getResult(JSONObject obj) {
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
