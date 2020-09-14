package com.sjl.core.util;

import com.sjl.core.util.log.LogUtils;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ThreadUtils
 * @time 2020/9/13 11:08
 * @copyright(C) 2020 song
 */
public class ThreadUtils {
    /**
     * 查询所有线程
     * @return
     */
    public static Thread[] findAllThread() {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();

        while (currentGroup.getParent() != null) {
            // 返回此线程组的父线程组
            currentGroup = currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();

        Thread[] lstThreads = new Thread[noThreads];
        //把对此线程组中的所有活动子组的引用复制到指定数组中。
        currentGroup.enumerate(lstThreads);

        for (Thread thread : lstThreads) {
            LogUtils.i("线程数量：" + noThreads + " 线程id：" + thread.getId() + " 线程名称：" + thread.getName() + " 线程状态：" + thread.getState());
        }
        return lstThreads;
    }
}
