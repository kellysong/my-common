package com.sjl.core.net.file;

/**
 * 文件回调
 */

public interface FileCallback<T> {

    /**
     * 进度，运行在主线程
     *
     * @param progress 下载进度
     * @param total    总大小
     * @param speed    下载速率
     * @param id       文件id,多个文件上传使用，文件下载恒为-1
     */
    void onProgress(int progress, long total, long speed, int id);

    /**
     * 运行在主线程
     *
     * @param file
     */
    void onCompleted(T file);

    /**
     * 运行在主线程
     *
     * @param e
     */
    void onError(Throwable e);

}
