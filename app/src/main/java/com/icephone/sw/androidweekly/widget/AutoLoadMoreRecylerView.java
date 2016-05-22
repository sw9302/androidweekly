package com.icephone.sw.androidweekly.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by sw on 2016/5/9.
 */
public class AutoLoadMoreRecylerView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "AutoLoadMoreRecylerView";
    private LoadMoreListView mListView;
    private RefreshListener mRefreshListener;
    private boolean isNeedRefresh = false;
    private boolean isNeedLoadMore = false;

    @Override
    public void onRefresh() {
//        if(isRefreshing()){
//            return;
//        }
        if(mRefreshListener != null) {
            mRefreshListener.refresh();
        }
    }

    public interface AutoLoadMoreListener {
        void loadNextPage();
    }

    public interface RefreshListener{
        void refresh();
    }

    public AutoLoadMoreRecylerView(Context context){
        super(context);
        init();
    }
    public AutoLoadMoreRecylerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setRefreshListener(@NonNull RefreshListener listener){
        this.mRefreshListener = listener;
    }

    private void init(){
        setEnabled(isNeedRefresh);
        mListView = new LoadMoreListView(getContext());
        this.addView(mListView);
        this.setOnRefreshListener(this);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        if(mListView != null){
            mListView.setAdapter(adapter);
        }
    }

    public void setAutoLoadMoreListener(AutoLoadMoreListener listener){
        if(mListView != null){
            mListView.setAutoLoadMoreListener(listener);
        }
    }

    public void setLoadState(boolean isLoading){
        if(!isLoading){
            setRefreshing(false);
        }
        if (mListView != null){
            mListView.setLoadState(isLoading);
        }
    }

    public void isNeedRefresh(boolean isNeedRefresh){
        this.isNeedRefresh = isNeedRefresh;
        setEnabled(isNeedRefresh);
    }

    public boolean isAllowRefresh(){
        return isNeedRefresh;
    }

    public boolean isAllowLoadMore(){
        return isNeedLoadMore;
    }

    public void isNeedLoadMore(boolean isNeedLoadMore){
        this.isNeedLoadMore = isNeedLoadMore;
        if(mListView != null){
            mListView.setIsNeedLoadMore(isNeedLoadMore);
        }
    }


    private static class LoadMoreListView extends RecyclerView{
        private LinearLayoutManager mLayoutManager;
        private AutoLoadMoreListener mAutoLoadMoreListener;
        private boolean isAllowLoadMore = false;
        private boolean isLoadingMore = false;

        public LoadMoreListView(Context context) {
            super(context);
            init();
        }

        private void init(){
            mLayoutManager = new LinearLayoutManager(getContext());
            this.setLayoutManager(mLayoutManager);
            this.setLayoutManager(mLayoutManager);
            this.addOnScrollListener(mOnScrollListener);
        }

        private RecyclerView.OnScrollListener mOnScrollListener
                = new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);

                int lastVisibleItem =
                        ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
                int firstVisibleItem =
                        ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                if(isAllowLoadMore){
                    if(lastVisibleItem + 1 >= totalItemCount && dy > 0){
                        if(isLoadingMore){
                            Log.d(TAG,"RecylerView is loading");
                        }else{
                            if(mAutoLoadMoreListener != null){
                                mAutoLoadMoreListener.loadNextPage();
                                isLoadingMore = true;
                            }
                        }
                    }
                }else{
                    
                }

            }

        };



        public void setAutoLoadMoreListener(@NonNull AutoLoadMoreListener listener){
            this.mAutoLoadMoreListener = listener;
        }

        public void setIsNeedLoadMore(boolean isNeedLoadMore){
            isAllowLoadMore = isNeedLoadMore;
        }
        public void setLoadState(boolean isLoadingMore){
            this.isLoadingMore = isLoadingMore;
        }
    }


}
