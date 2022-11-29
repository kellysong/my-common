/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjl.core.manager;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(Executors.newFixedThreadPool(THREAD_COUNT), Executors.newCachedThreadPool(),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }

        public void execute(@NonNull Runnable command, long delayMillis) {
            mainThreadHandler.postDelayed(command, delayMillis);
        }
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public void runMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            mainThread.execute(runnable);
        }
    }

    /**
     * 运行在主线程，待延迟执行
     *
     * @param runnable
     */
    public void runMainThreadDelay(Runnable runnable, long delayMillis) {
        ((MainThreadExecutor) mainThread).execute(runnable, delayMillis);
    }

    /**
     * 运行同一个子线程，如果当前任务所在线程不是主线程则复用当前线程，否则新开线程处理任务
     *
     * @param runnable
     */
    public void runSameThread(Runnable runnable) {
        if (!isMainThread()) {
            runnable.run();
        } else {
            diskIO.execute(runnable);
        }

    }

    /**
     * 运行在子线程
     *
     * @param runnable
     */
    public void runThread(Runnable runnable) {
        diskIO.execute(runnable);
    }
}
