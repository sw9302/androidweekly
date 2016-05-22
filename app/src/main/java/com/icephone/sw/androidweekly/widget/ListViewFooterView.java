package com.icephone.sw.androidweekly.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sw on 2016/5/10.
 */
public class ListViewFooterView extends LinearLayout{
    public ListViewFooterView(Context context) {
        super(context);
        initView();
    }
    private void initView(){
        TextView mTextView = new TextView(getContext());
        mTextView.setText("加载中...");
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120);
        llp.gravity = Gravity.CENTER;
        this.addView(mTextView,llp);
    }
}
