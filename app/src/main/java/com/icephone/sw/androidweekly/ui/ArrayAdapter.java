package com.icephone.sw.androidweekly.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.icephone.sw.androidweekly.widget.ListViewFooterView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sw on 2016/5/8.
 */
public abstract class ArrayAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mData;
    protected boolean mDataValid;
    private static final int TYPE_ITEM = 2;
//    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean isAlllowLoadMore = false;
    private boolean mHasFoot = false;

    public ArrayAdapter(Context context) {
        mContext = context;
        mDataValid = false;
    }

    public abstract void bindView(View view,T t,int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType == TYPE_FOOTER){
            Log.e("ArrayAdapter","Footer");
            return new FooterViewHolder(new ListViewFooterView(mContext));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!mDataValid){
            throw new IllegalStateException("this should only be called when the data is valid");
        }
        if(position < 0 || position >= mData.size() + 1){
            throw new IllegalStateException("couldn't get view at this position" + position);
        }
        if(position > mData.size() - 1){
            return;
        }
        T t = mData.get(position);
        bindView(holder.itemView,t,position);
    }

    @Override
    public int getItemCount() {
        if(mDataValid && mData != null){
            return mData.size() + (mHasFoot ? 1 : 0);
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void updateData(List<T> data,boolean pushStack,boolean isRefresh){
        int startIndex = 0;
        int endIndex = 0;
        if(data == null || data.size() == 0){
            mDataValid = false;
            return;
        }
        mDataValid = true;

        if(mData == null){
            mData = new ArrayList<T>(40);
        }

        if(isRefresh){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
            return;
        }

        if(pushStack){
            startIndex = 0;
            endIndex = data.size() - 1;
        }else{
            startIndex = mData.size();
            endIndex = startIndex + data.size() - 1;
        }

        for(T t:data){
            if(pushStack){
                mData.add(0,t);
            }else{
                mData.add(t);
            }
        }
        postUpdateData();
        notifyItemRangeChanged(startIndex,endIndex);
    }


    protected void postUpdateData() {

    }

    public void setLoadMore(boolean isAlllowLoadMore){
        this.isAlllowLoadMore = isAlllowLoadMore;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (mHasFoot && position == mData.size() && isAlllowLoadMore) {
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    public void setHasFoot(boolean hasFoot){
        mHasFoot = hasFoot;
        notifyDataSetChanged();
    }

    public List<T> getData(){
        if(mData != null){
            return mData;
        }else{
            return new ArrayList<T>();
        }
    }

    public void clear(){
        if(mData != null && mData.size() > 0){
            notifyItemMoved(0,mData.size() - 1);
            mData.clear();
        }
    }

}
