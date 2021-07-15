package com.sjl.core.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.AppUtils;
import com.sjl.core.util.log.LogUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * base Observer
 * 缺点：如何任务耗时较长退出activity，可能不回调_onComplete、_onError
 *
 * @author songjiali
 * @version 1.0.0
 * @filename RxObserver.java
 * @time 2018/10/24 18:22
 * @copyright(C) 2018 song
 */
public abstract class RxObserver<T> implements Observer<T> {
    protected Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }


    @Override
    public void onError(Throwable e) {
        LogUtils.e(e);
        String msg;
        if (AppUtils.isConnected(BaseApplication.getContext())) {
            msg = "当前网络不可用";
        } else if (e instanceof UnknownHostException || e instanceof NoRouteToHostException) {
            msg = "该地址不存在，请检查";
        } else if (e instanceof ConnectException) {
            msg = "连接异常或被拒绝，请检查";
        } else if (e instanceof HttpException) {
            msg = " http错误";
        } else if (e instanceof InterruptedIOException) { //  连接超时
            msg = " http 连接超时";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            msg = " 数据解析错误";
        } else if (e instanceof SocketTimeoutException) {
            // 超时
            msg = "socket连接超时";
        } else {
            msg = "未知错误，错误原因：" + e.getMessage();
        }
        _onError(msg);

    }

    @Override
    public void onComplete() {
        _onComplete();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String msg);

    public  void _onComplete(){}
}
