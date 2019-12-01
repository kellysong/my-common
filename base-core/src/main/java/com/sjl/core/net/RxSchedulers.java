package com.sjl.core.net;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用的Rx线程转换类
 * Rxjava默认运行在当前线程中。如果当前线程是子线程，则rxjava运行在子线程；同样，当前线程是主线程，则rxjava运行在主线程
 */
public class RxSchedulers {


    static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return (upstream).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };


    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }


    /**
     * 事件产生和消费在io线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_io() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }


    static final SingleTransformer singleTransformer = new SingleTransformer() {

        @Override
        public SingleSource apply(Single upstream) {
            return (upstream).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T> SingleTransformer<T, T> applySingle() {
        return (SingleTransformer<T, T>) singleTransformer;
    }


}
