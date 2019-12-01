package com.zhy.adapter.viewpager;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

/**
 * 抽象的PagerAdapter实现类,封装了内容为View,数据为SparseArray类型的适配器实现.
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PagerSparseAdapter.java
 * @time 2018/3/1 10:35
 * @copyright(C) 2018 song
 */
public abstract class PagerSparseAdapter<T> extends BasePagerAdapter {
    protected SparseArray<T> mData;
    protected Context mContext;

    public PagerSparseAdapter(Context context, SparseArray<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public abstract View newView(int position);

    public T getItem(int position) {
        return mData.valueAt(position);
    }
}
