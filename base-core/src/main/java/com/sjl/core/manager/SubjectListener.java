package com.sjl.core.manager;

/**
 * 被观察者接口
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SubjectListener.java
 * @time 2018/10/5 17:53
 * @copyright(C) 2018 xxx有限公司
 */
public interface SubjectListener {

    void add(ObserverListener observerListener);

    void notifyObserver(int progress);

    void remove(ObserverListener observerListener);
}
