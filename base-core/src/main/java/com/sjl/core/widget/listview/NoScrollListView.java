package com.sjl.core.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不滚动的listview
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NoScrollListView.java
 * @time 2018/4/17 10:27
 * @copyright(C) 2018 song
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
