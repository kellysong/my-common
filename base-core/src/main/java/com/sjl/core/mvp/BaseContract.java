package com.sjl.core.mvp;


public interface BaseContract {

    interface IBasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface IBaseView {


    }
}
