package com.icephone.sw.androidweekly.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icephone.sw.androidweekly.R;
import com.icephone.sw.androidweekly.model.WeeklyInfo;

/**
 * Created by sw on 2016/5/7.
 */
public class WeeklyItem extends LinearLayout{

    private TextView mTitleText;
    private TextView mSubText;
    private TextView mTimeText;

    private int mPosition;

    public WeeklyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindData(WeeklyInfo info,int position){
        if(info == null){
            return;
        }
        mTitleText.setText(info.getTitle());
        mTimeText.setText(info.getTime());
        mSubText.setText(info.getSubTitle());
        mPosition = position;
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        mTimeText = (TextView)findViewById(R.id.time_text);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mSubText = (TextView) findViewById(R.id.sub_text);
    }
}
