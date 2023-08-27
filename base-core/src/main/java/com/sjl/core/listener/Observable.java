package com.sjl.core.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 *
 * @author Kelly
 * @version 1.0.0
 * @filename Observable
 * @time 2023/4/6 14:00
 * @copyright(C) 2023 song
 */
public class Observable<T> {
    /**
     * 观察者接口集合
     */
    private  List<Observer<T>> mObservers = new ArrayList<Observer<T>>();

    public synchronized void register(Observer<T> observer) {
        if (observer == null) {
            throw new NullPointerException("observer为空");
        }
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }

    }

    public synchronized void unregister(Observer<T> observer) {
        mObservers.remove(observer);
    }

    public void notifyObservers(T data) {
        for (Observer<T> observer : mObservers) {
            observer.onUpdate(data);
        }
    }

    public List<Observer<T>> getObservers() {
        return mObservers;
    }

    public synchronized void clear() {
        mObservers.clear();
    }
}