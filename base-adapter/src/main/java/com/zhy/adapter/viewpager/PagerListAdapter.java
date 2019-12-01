package com.zhy.adapter.viewpager;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的PagerAdapter实现类,封装了内容为View,数据为List类型的适配器实现.
 *
 * @author Kelly
 * @version 1.0.0
 * @filename PagerListAdapter.java
 * @time 2018/3/1 10:34
 * @copyright(C) 2018 song
 */
public abstract class PagerListAdapter<T> extends BasePagerAdapter {
    protected Context mContext;

    protected List<T> mData;

    public PagerListAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public abstract View newView(int position);

    public T getItem(int position) {
        return mData.get(position);
    }

    public void setData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }
}
