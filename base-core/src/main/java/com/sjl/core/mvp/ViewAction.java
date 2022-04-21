package com.sjl.core.mvp;

/**
 * 用于处理V调用空指针问题
 * @param <V>
 */
public interface ViewAction<V extends BaseContract.IBaseView> {

    void call(V v);
}