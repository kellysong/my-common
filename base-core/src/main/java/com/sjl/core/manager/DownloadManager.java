package com.sjl.core.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/9/29.
 * Subject（被观察者）
 * 关联行为场景，这个关联是可拆分的。将观察者和被观察者封装在不同的对象中，可以各自独立的变化。
 * 当一个对象改变时，有其他对象要进行相应的变化，但是他并不知道有多少个对象需要变化。
 * 跨系统的消息交换长江，如消息队列，时事件总线等
 * <p>
 * 观察者模式代替广播模式更新ui,观察者模式类关系：
 * Subject : 抽象被观察者（Observeable），吧所有观察者对象的医用保存在一个集合里，每个主题都可以有任意数量的观察者，抽象被观察者提供一个接口，可以增加和删除观察者对象。
 * ConcreteSubject： 具体的被观察者，将有关状态存入具体的观察者对象，在具体的被观察者内部状态发生变化时，给所有注册的观察者发送通知。
 * Observer ： 抽象观察者，定义了一个更新接口，使得在得到被观察者的通知时更新自己。
 * ConcreteObserver ： 具体的观察者，实现了抽象观察者锁定义的接口，用来在收到通知时更新自己。
 * 应用场景：
 */

public class DownloadManager implements SubjectListener {
    private static DownloadManager downloadManager;

    //观察者接口集合
    private List<ObserverListener> observers = new ArrayList<ObserverListener>();

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (downloadManager == null) {
            synchronized (DownloadManager.class) {
                if (downloadManager == null){
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }

    /**
     * 加入监听队列
     */
    @Override
    public void add(ObserverListener observerListener) {
        if (observerListener == null) {
            throw new RuntimeException();
        }
        if (!observers.contains(observerListener)) {
            observers.add(observerListener);

        }
    }


    /**
     * 监听队列中移除
     *
     * @param observerListener
     */
    @Override
    public void remove(ObserverListener observerListener) {
        if (observerListener == null) {
            throw new RuntimeException();
        }
        if (observers.contains(observerListener)) {
            observers.remove(observerListener);
        }
    }

    /**
     * 通知观察者刷新数据
     *
     * @param progress
     */
    @Override
    public void notifyObserver(int progress) {
        for (ObserverListener observer : observers) {
            observer.update(progress);
        }
    }

}
