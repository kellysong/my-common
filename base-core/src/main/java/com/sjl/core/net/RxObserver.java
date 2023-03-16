package com.sjl.core.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.sjl.core.net.exception.BaseException;
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
        String msg;
        int code = HttpErrorCode.ERROR_UNKNOWN;
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            msg = baseException.errorMsg;
            code = baseException.errorCode;
        } else if (e instanceof UnknownHostException) {
            msg = "未知地址，请检查";
            code =  HttpErrorCode.ERROR_NETWORK_BAD;
        } else if (e instanceof ConnectException) {
            msg = "连接异常或被拒绝，请检查";
            code = HttpErrorCode.ERROR_NETWORK_BAD;
        } else if (e instanceof SocketTimeoutException) {
            msg = "连接超时";
            code =  HttpErrorCode.ERROR_NETWORK_BAD;
        } else if (e instanceof HttpException) {
            msg = " http错误";
            code = HttpErrorCode.ERROR_HTTP_ERROR;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            msg = " 数据解析错误";
            code = HttpErrorCode.ERROR_PARSE;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            msg = "证书验证失败";
            code =  HttpErrorCode.ERROR_SSL;
        } else {
            msg = "未知错误，错误原因：" + e.getMessage();
        }
        _onError(code, msg);
        LogUtils.e("rx http请求异常,code:" + code + ",msg:" + msg,e);

        cancel();
    }

    protected void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

    @Override
    public void onComplete() {
        _onComplete();
        cancel();
    }

    @Override
    public void onNext(T t) {
        //捕获异常，防止回调到全局异常
        try {
            _onNext(t);
        } catch (Throwable e) {
            _onNextError(e);
        }
    }
    /**
     * 成功回调
     *
     * @param t
     */
    public abstract void _onNext(T t);

    /**
     * 数据响应异常，一般是不会触发，选择性处理，与onError互斥
     * @param e
     */
    protected void _onNextError(Throwable e) {
        LogUtils.e("数据转换异常",e);
        _onError(HttpErrorCode.ERROR_TRANSFORMER,"数据转换异常");
    }

    /**
     * 错误回调
     *
     * @param code
     * @param msg
     */
    public abstract void _onError(int code, String msg);

    public void _onComplete() {
    }
}
