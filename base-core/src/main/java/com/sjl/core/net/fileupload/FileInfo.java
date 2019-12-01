package com.sjl.core.net.fileupload;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileInfo.java
 * @time 2018/8/14 9:29
 * @copyright(C) 2018 song
 */
public class FileInfo {
    private long currentBytes;
    private long contentLength;
    private boolean done;

    public FileInfo(long currentBytes, long contentLength, boolean done) {
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
