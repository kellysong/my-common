package com.sjl.lib.net.rxjava;

import com.sjl.lib.entity.ResponseData;
import com.sjl.lib.net.DataResponseException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 错误转换(RxJava专用)
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ErrorTransformer
 * @time 2020/9/14 10:30
 * @copyright(C) 2020 song
 */
public class ErrorTransformer<T> implements ObservableTransformer<ResponseData<T>, T> {

    @Override
    public ObservableSource apply(Observable upstream) {
        return (Observable<T>) upstream.map(new Function<ResponseData<T>, T>() {
            @Override
            public T apply(ResponseData<T> response) throws Exception {
                if (response.getErrorCode() != 0) {// 0代表请求成功，统一处理请求,避免在每次网络请求的时候对status进行重复判断
                    throw new DataResponseException(response.getErrorCode(), response.getErrorMsg());
                }
                return response.getData();
            }
        });
    }
}

