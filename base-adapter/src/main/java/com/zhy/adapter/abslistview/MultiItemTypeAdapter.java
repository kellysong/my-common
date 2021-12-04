package com.zhy.adapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zhy.adapter.abslistview.base.ItemViewDelegate;
import com.zhy.adapter.abslistview.base.ItemViewDelegateManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class MultiItemTypeAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;

    private ItemViewDelegateManager mItemViewDelegateManager;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        this.mContext = context;
//        this.mDatas = datas;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;

        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager())
            return mItemViewDelegateManager.getItemViewDelegateCount();
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            int viewType = mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(mDatas.get(position), position);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder = null;
        if (convertView == null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent,
                    false);
            viewHolder = new ViewHolder(mContext, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
            onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }


        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    protected void convert(ViewHolder viewHolder, T item, int position) {
        mItemViewDelegateManager.convert(viewHolder, item, position);
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * 刷新列表
     *
     * @param datas
     * @deprecated Use {@link MultiItemTypeAdapter#setNewData(List)} directly
     */
    @Deprecated
    public void refreshItems(List<T> datas) {
        setNewData(datas);
    }

    /**
     * 局部更新条目，调用一次getView()方法；Google推荐的做法
     *
     * @param position 要更新的位置
     */
    public void setData(int position, ListView listView) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }

    }

    /**
     * 交换位置
     *
     * @param startPosition
     * @param endPosition
     */
    public synchronized void change(int startPosition, int endPosition) {
        int i = getCount() - 1;
        if (startPosition >= 0 && startPosition <= i && endPosition >= 0 && endPosition <= i) {
            T srcData = mDatas.get(startPosition);
            mDatas.remove(srcData);
            mDatas.add(endPosition, srcData);
            notifyDataSetChanged();
        }

    }

    /**
     * 更新单个条目
     */
    public void setData(int index, @NonNull T data) {
        mDatas.set(index, data);
        notifyDataSetChanged();
    }

    /**
     * 填充新的数据
     *
     * @param datas
     */
    public void setNewData(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加新的数据
     *
     * @param position
     * @param newData
     */
    public void addData(int position, List<T> newData) {
        mDatas.addAll(position, newData);
        notifyDataSetChanged();
    }

    /**
     * 添加新的数据
     *
     * @param newData
     */
    public void addData(List<T> newData) {
        mDatas.addAll(newData);
        notifyDataSetChanged();
    }

    /**
     * 删除条目
     *
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 批量删除条目
     *
     * @param value
     */
    public void remove(List<T> value) {
        mDatas.removeAll(value);
        notifyDataSetChanged();
    }


    /**
     * 返回列表数据
     *
     * @return
     */
    public List<T> getData() {
        return mDatas;
    }
}
