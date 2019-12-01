package com.sjl.core.net.fileupload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 自定义callback
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitCallback.java
 * @time 2018/8/13 17:28
 * @copyright(C) 2018 song
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()) {
            onSuccess(call, response);
        } else {
            onFailure(call, new Throwable(response.message()));
        }
    }
    public abstract void onSuccess(Call<T> call, Response<T> response);

    /**
     * 用于进度的回调
     * @param currentBytes
     * @param contentLength
     * @param done
     */
    public abstract void onProgress(long currentBytes, long contentLength, boolean done) ;
}
