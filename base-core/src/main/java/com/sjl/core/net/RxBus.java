package com.sjl.core.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * 事件总线的好处在于方便组件之间的交互，RxBus不是一个库，而是使用RxJava实现事件总线的一种思想。
 * 相对于使用eventbus，使用rxbus来的更方便。
 * 原理:PublishSubject本身作为观察者和被观察者。
 */

public class RxBus {
    private static volatile RxBus sInstance;
    /**
     * 事件总线
     */
    private final PublishSubject<Object> mEventBus;
    /**
     * 粘性事件存储
     */
    private final Map<Integer, Object> mStickyEventMap;

    private RxBus() {
        mEventBus = PublishSubject.create();
        mStickyEventMap = new ConcurrentHashMap<>();

    }

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }


    /**
     * 返回Event的管理者,进行对事件的接受
     *
     * @return
     */
    public Observable toObservable() {
        return mEventBus;
    }


    /**
     * 发送事件(post event)
     *
     * @param event : event object(事件的内容)
     */
    public void post(Object event) {
        mEventBus.onNext(event);
    }


    /**
     * @param cls :保证接受到指定的类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> cls) {
        //ofType起到过滤的作用,确定接受的类型
        return mEventBus.ofType(cls);
    }


    /**
     * 发送普通事件
     *
     * @param code  事件码
     * @param event 发送事件对象
     */
    public void post(int code, Object event) {
        Message msg = new Message(code, event);
        mEventBus.onNext(msg);
    }

    /**
     * 普通事件注册
     *
     * @param code 发送事件代码
     * @param cls  发送事件对象对应的class
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> cls) {
        return mEventBus.ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(Message msg) throws Exception {
                        return msg.code == code && cls.isInstance(msg.event);
                    }
                })
                .map(new Function<Message, T>() {
                    @Override
                    public T apply(Message msg) throws Exception {
                        return (T) msg.event;
                    }
                });

    }

    class Message {
        /**
         * 发送事件代码
         */
        int code;
        /**
         * 发送事件对象
         */
        Object event;

        public Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }

    //下面是Sticky事件封装


    /**
     * 发送一个新Sticky事件
     *
     * @param code  发送事件代码
     * @param event 发送事件对象
     */
    public void postSticky(int code, Object event) {
        Message msg = new Message(code, event);
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(code, msg);
        }
        mEventBus.onNext(msg);
    }


    /**
     * Sticky事件注册
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code 发送事件代码
     * @param cls  发送事件对象对应的class
     */
    public <T> Observable<T> toObservableSticky(final int code, final Class<T> cls) {
        synchronized (mStickyEventMap) {
            final Object event = mStickyEventMap.get(code);
            if (event != null) {
                return mEventBus.ofType(Message.class)
                        .mergeWith(Observable.create(new ObservableOnSubscribe<Message>() {
                            @Override
                            public void subscribe(ObservableEmitter<Message> emitter) throws Exception {
                                emitter.onNext((Message) event);
                            }
                        })).filter(new Predicate<Message>() {
                            @Override
                            public boolean test(Message msg) throws Exception {
                                return msg.code == code && cls.isInstance(msg.event);
                            }
                        })
                        .map(new Function<Message, T>() {
                            @Override
                            public T apply(Message msg) throws Exception {
                                return (T) msg.event;
                            }
                        });
            } else {
                return toObservable(code, cls);

            }

        }
    }


    /**
     * 根据eventType获取Sticky事件
     */
    public Message getStickyEvent(int code) {
        synchronized (mStickyEventMap) {
            return (Message) mStickyEventMap.get(code);
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public Message removeStickyEvent(int code) {
        synchronized (mStickyEventMap) {
            return (Message) mStickyEventMap.remove(code);
        }
    }


    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
