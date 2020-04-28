package com.zhy.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ItemViewDelegateManager;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhy on 16/4/9.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
//        mDatas = datas;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


     /*以下方法由本人添加*/

    /**
     * 删除条目 add by Kelly on 20180503
     *
     * @param position
     */
    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);//删除动画
        if (position != mDatas.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, mDatas.size() - position);//itemview重新onBind，就可以了
        }
    }


    /**
     * 批量删除条目
     * @param value
     */
    public void batchRemoveItem(List<T> value){
        mDatas.removeAll(value);
        notifyDataSetChanged();
    }


    /**
     * 添加条目 add by Kelly on 20180503
     *
     * @param position
     * @param data
     */
    @Deprecated
    public void addItem(int position, T data) {
        mDatas.add(position, data);
        //添加动画
        notifyItemInserted(position);
    }


    /**
     * 刷新条目
     *
     * @param datas
     */
    public void refreshItems(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 追加条目
     *
     * @param datas
     */
    public void appendItems(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获取数据条目
     */
    public T getDataItem(int position) {
        return mDatas.get(position);
    }

    protected Context getContext() {
        return mContext;
    }

}
