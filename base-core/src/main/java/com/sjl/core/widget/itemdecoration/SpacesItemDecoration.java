package com.sjl.core.widget.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 适用于列表布局
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SpacesItemDecoration
 * @time 2021/12/6 15:32
 * @copyright(C) 2021 song
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    //是否包含边缘
    private boolean includeEdge;

    public SpacesItemDecoration(int space, boolean includeEdge) {
        this.space = space;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (!includeEdge) {
            //上下左右边缘无边距
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            if (childAdapterPosition == 0) {
                outRect.top = 0;
            } else {
                outRect.top = space;
            }
        } else {
            //上下左右边缘有边距
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

}