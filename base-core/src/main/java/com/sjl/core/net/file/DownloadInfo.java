package com.sjl.core.net.file;

import java.io.File;

/**
 * 下载文件信息
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DownloadInfo.java
 * @time 2018/7/25 14:27
 * @copyright(C) 2018 song
 */
public class DownloadInfo {
    private File file;
    private String fileName;
    /**
     * 单位 byte
     */
    private long fileSize;
    /**
     * 当前下载大小
     */
    private long currentSize;
    /**
     * 当前下载进度
     */
    private int progress;
    /**
     * 下载速率
     */
    private long speed;




    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

}
