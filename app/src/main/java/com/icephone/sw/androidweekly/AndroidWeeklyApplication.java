package com.icephone.sw.androidweekly;

import android.app.Application;
import android.content.Context;

/**
 * Created by sw on 2016/5/22.
 */
public class AndroidWeeklyApplication extends Application {
    private static Context mContext;
    private static AndroidWeeklyApplication mApplication;

    public AndroidWeeklyApplication(){
        super();
        mApplication = this;
        mContext = getApplicationContext();
    }
    @Override
    public void onCreate(){
        super.onCreate();

    }

    public static AndroidWeeklyApplication getApplication(){
        return mApplication;
    }

    public static Context getContext(){
        return mContext;
    }
}
