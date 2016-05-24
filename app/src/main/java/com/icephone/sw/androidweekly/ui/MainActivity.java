package com.icephone.sw.androidweekly.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.icephone.sw.androidweekly.ApiManager;
import com.icephone.sw.androidweekly.R;
import com.icephone.sw.androidweekly.model.WeeklyInfo;
import com.icephone.sw.androidweekly.network.BaseOKHttpClient;
import com.icephone.sw.androidweekly.utils.Constant;
import com.icephone.sw.androidweekly.widget.AutoLoadMoreRecylerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        AutoLoadMoreRecylerView.AutoLoadMoreListener,
        AutoLoadMoreRecylerView.RefreshListener,
        BaseOKHttpClient.OnLoadData<ArrayList<WeeklyInfo>>{
    private static final String TAG = "MainActivity";
    private AutoLoadMoreRecylerView mListView;
    private WeeklyAdapter mAdapter;
    private ApiManager mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (AutoLoadMoreRecylerView) findViewById(R.id.listview);
        mAdapter = new WeeklyAdapter(this);
        mAdapter.setLoadMore(false);
        mListView.setAdapter(mAdapter);
        mListView.setAutoLoadMoreListener(this);
        mListView.setRefreshListener(this);
        mListView.isNeedLoadMore(false);
//        loadData(1);
        mApiManager = new ApiManager(this,Constant.WEEKLY_URL);
        mApiManager.setLoadDataListener(this);
        mApiManager.loadData();
    }

    @Override
    public void loadNextPage() {
       mApiManager.loadData();
    }

    @Override
    public void onCompleted(ArrayList<WeeklyInfo> weeklyInfos, BaseOKHttpClient.LoadState state) {
        boolean isRefresh = (state == BaseOKHttpClient.LoadState.CLEAR);
        if(weeklyInfos != null ){
            mAdapter.updateData(weeklyInfos,false,isRefresh);
        }

        mListView.setLoadState(false);
    }

    @Override
    public void onNext(ArrayList<WeeklyInfo> weeklyInfos) {

    }

    @Override
    public void refresh() {
        Log.e(TAG,"Refresh");
        mApiManager.reset();
        mApiManager.loadData();
    }

}
