package com.icephone.sw.androidweekly.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icephone.sw.androidweekly.R;
import com.icephone.sw.androidweekly.model.WeeklyInfo;
import com.icephone.sw.androidweekly.widget.WeeklyItem;

/**
 * Created by sw on 2016/5/8.
 */
public class WeeklyAdapter extends ArrayAdapter<WeeklyInfo> {
    private LayoutInflater mLayoutInflater;

    public WeeklyAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, WeeklyInfo weeklyInfo, int position) {
        ((WeeklyItem)view).bindData(weeklyInfo,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent,viewType);
        if(holder == null){
            return new Holder(mLayoutInflater.inflate(R.layout.weekly_item,parent,false));
        }
        return holder;

    }

}
