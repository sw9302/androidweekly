package com.icephone.sw.androidweekly;

import android.app.Application;
import android.content.Context;

/**
 * Created by sw on 2016/5/22.
 */
public class AndroidWeeklyApplication extends Application {
    private static Context mContext;
    private static AndroidWeeklyApplication mApplication;

    @Override
    public void onCreate(){
        super.onCreate();
        mApplication = this;
        mContext = getApplicationContext();
    }

    public static AndroidWeeklyApplication getApplication(){
        return mApplication;
    }

    public static Context getContext(){
        return mContext;
    }
}
