package com.zhy.adapter.viewpager;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * PagerAdapter基类,封装了内容为View的公共操作
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BasePagerAdapter.java
 * @time 2018/3/1 10:31
 * @copyright(C) 2018 song
 */
public abstract class BasePagerAdapter extends PagerAdapter {
    protected SparseArray<View> mViews;


    protected OnItemClickListener mOnItemClickListener;
    protected OnTouchActionListener mOnTouchActionListener;

    public BasePagerAdapter() {
        mViews = new SparseArray<View>();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = newView(position);
            mViews.put(position, view);
        }
        setOnClickListener(view, position);
        setOnTouchListener(view);
        container.addView(view);
        return view;
    }


    public abstract View newView(int position);

    /**
     * 更新指定位置的视图
     *
     * @param position
     */
    public void notifyUpdateView(int position) {
        View view = updateView(mViews.get(position), position);
        mViews.put(position, view);
        notifyDataSetChanged();
    }

    /**
     * 更新视图的具体操作
     *
     * @param view
     * @param position
     * @return
     */
    public View updateView(View view, int position) {
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    /**
     * 设置绑定View点击事件
     *
     * @param view
     * @param position
     */
    private void setOnClickListener(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }


    /**
     * 设置绑定View触摸事件
     *
     * @param view
     */
    private void setOnTouchListener(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                int action = ev.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (mOnTouchActionListener != null) {
                            mOnTouchActionListener.onDown();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mOnTouchActionListener != null) {
                            mOnTouchActionListener.onUp();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 条目点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 条目触摸动作事件
     */
    public interface OnTouchActionListener {
        void onDown();

        void onUp();

    }

    public void setOnTouchActionListener(OnTouchActionListener onTouchActionListener) {
        this.mOnTouchActionListener = onTouchActionListener;
    }

}