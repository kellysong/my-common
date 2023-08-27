package com.sjl.core.listener;

/**
 * 观察者
 *
 * @author Kelly
 * @version 1.0.0
 * @filename Observer
 * @time 2023/4/6 13:58
 * @copyright(C) 2023 song
 */
public interface Observer<T> {
    void onUpdate(T data);
}