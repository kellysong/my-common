package com.sjl.core.net;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DataCallback.java
 * @time 2019/9/29 10:47
 * @copyright(C) 2019 song
 */
public interface DataCallback<T> {
    void onSuccess(T result);

    void onError(Throwable e);
}