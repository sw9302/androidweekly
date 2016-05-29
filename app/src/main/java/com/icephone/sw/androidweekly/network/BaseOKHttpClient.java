package com.icephone.sw.androidweekly.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.icephone.sw.androidweekly.utils.Constant;

import org.json.JSONObject;

import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by sw on 2016/5/23.
 */
public abstract class BaseOKHttpClient<T>{

    private static final String TAG = "BaseOkHttpClient";
    private String mUrl;
    private ArrayMap<String,String> mParams = new ArrayMap<String, String>();
    private int mPage = 1;
    private OnLoadData mLoadDataListener;
    private OkHttpClient mClient;
    protected Context mContext;

    public void setLoadDataListener(OnLoadData loadDataListener){
        mLoadDataListener = loadDataListener;
        mClient = new OkHttpClient();
    }

    public enum LoadState{
        CLEAR,
        OK
    }

    public interface OnLoadData<T>{
        void onCompleted(T t,LoadState state);
        void onNext(T t);
    }

    public BaseOKHttpClient(Context ctx,String url){
        mContext = ctx;
        mUrl = url;
    }

    public void reset(){
        mPage = 1;
        mParams.clear();
    }

    public void loadData(){
        Observable<T> databaseObservable = databaseObservable();
        if(databaseObservable != null){
            databaseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<T>() {
                        T t = null;
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted()");
                            if(t != null){
                                if(mLoadDataListener != null){
                                    LoadState state;
                                    if(mPage == 1){
                                        state = LoadState.CLEAR;
                                    }else{
                                        state = LoadState.OK;
                                    }
                                    mLoadDataListener.onCompleted(t,state);
                                }else{
                                    Log.e(TAG,"LoadDataListener is null");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "database onError()");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(T t) {
                            Log.d(TAG, "database onNext()");
                            this.t = t;
                        }
                    });
        }else{
            Log.e(TAG,"databaseObservable is null");
        }

        init();

    }

    public void forceLoad(){
        init();
    }

    private void init(){
        Observable<T> serverObservable = serverObservable();
        if(serverObservable != null){
            serverObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<T>() {
                        T t = null;
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "server onCompleted()");
//                            if(t != null){
                                if(mLoadDataListener != null){
                                    LoadState state;
                                    if(mPage == 1){
                                        state = LoadState.CLEAR;
                                    }else{
                                        state = LoadState.OK;
                                    }
                                    mLoadDataListener.onCompleted(t,state);
                                }else{
                                    Log.e(TAG,"LoadDataListener is null");
                                }
                                mPage++;
                                if(mParams.containsKey(Constant.PAGE_INDEX)){
                                    mParams.remove(Constant.PAGE_INDEX);
                                }
//                            }
                            if(t == null){
                                Toast.makeText(mContext,"没有更多了",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "server onError()");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(T t) {
                            Log.d(TAG, "server onNext()");
                            this.t = t;
                        }
                    });
        }else{
            Log.e(TAG,"serverObservable is null");
        }
    }

    public abstract JSONObject getFromDatabse();
    public abstract void saveToDatabase(JSONObject obj);

    private Observable<T> databaseObservable(){
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return Observable.just(getResult(getFromDatabse()));
            }
        });
    }

    private Observable<T> serverObservable(){
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                Request request = new Request.Builder()
                        .url(getUrl()).build();
                try {
                    Response response = mClient.newCall(request).execute();
                    if(response != null && response.isSuccessful()){
                        ResponseBody body = response.body();
                        String data = body.string();
                        if(!TextUtils.isEmpty(data)){
                            Log.e(TAG,"body=" + data);
                            JSONObject obj = new JSONObject(data);
                            if(obj.optInt("errCode") == 200){
                                saveToDatabase(obj);
                                return Observable.just(getResult(obj));
                            }
                        }
                    }else{
                        Log.e(TAG,"code=" + response.code());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

        });

    }

    private String getUrl(){
        if(TextUtils.isEmpty(mUrl)){
            return null;
        }
        prepaerParams();
        if(!mParams.containsKey(Constant.PAGE_INDEX)){
            mParams.put(Constant.PAGE_INDEX,mPage + "");
        }else{
            String pageIndex = mParams.get(Constant.PAGE_INDEX);
            if(TextUtils.isDigitsOnly(pageIndex)){
                mPage = Integer.valueOf(pageIndex);
            }else{
                mParams.put(Constant.PAGE_INDEX,mPage + "");
            }
        }
        StringBuilder sb = new StringBuilder(mUrl);
        Set<String> keys = mParams.keySet();
        if(mUrl.charAt(mUrl.length() - 1) != '?'){
            sb.append("?");
        }
        for (String key:keys) {
            sb.append(key).append("=").append(mParams.get(key));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public abstract void prepaerParams();

    public abstract T getResult(JSONObject obj);

    public int getPageIndex(){return mPage;}
}
