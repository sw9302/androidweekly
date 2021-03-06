package com.icephone.sw.androidweekly.model;

import org.json.JSONObject;

/**
 * Created by sw on 2016/5/8.
 */
public class WeeklyInfo {

    private String mTitle;
    private String mTime;
    private String mSubTitle;

    public WeeklyInfo(JSONObject obj){
        if(obj == null){
            return;
        }
        mTime = obj.optString("time");
        mTitle = obj.optString("title");
        mSubTitle = obj.optString("sub_title");

    }
    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public String getSubTitle() {
        return mSubTitle;
    }
}
