package com.sjl.core.net.file;


import com.sjl.core.net.MainThreadExecutor;
import com.sjl.core.util.security.MD5Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 自定义文件上传请求体,用于实现文件上传进度显示
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileRequestBody.java
 * @time 2018/8/13 17:31
 * @copyright(C) 2018 song
 */
public final class FileRequestBody extends RequestBody {

    public static final int SEGMENT_SIZE = 4 * 1024;
    int id;
    /**
     * 实际请求体
     */
    private final File file;

    /**
     * 文件内容类型
     */
    protected String contentType;

    /**
     * 回调接口
     */
    private FileCallback fileCallback;



    public FileRequestBody(int id,File file, String contentType, FileCallback fileCallback) {
        super();
        this.id = id;
        this.file = file;
        this.contentType = contentType;
        this.fileCallback = fileCallback;

    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long len;
            int progress = 0;
            int lastProgress = -1;
            long contentLength = contentLength();
            // 开始上传时间
            long startTime = System.currentTimeMillis();
            int maxProgress = 100;
            //循环读取，写文件流
            while ((len = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += len;
                sink.flush();

                progress = (int) (total * 100 / contentLength);
                long curTime = System.currentTimeMillis();
                long usedTime = (curTime - startTime) / 1000;
                if (usedTime == 0) {
                    usedTime = 1;
                }
                long speed = (total / usedTime);
                if (progress != lastProgress) {
                    if (fileCallback != null) {
                        int finalProgress = progress;
                        MainThreadExecutor.runMainThread(new Runnable() {
                            @Override
                            public void run() {
                                fileCallback.onProgress(finalProgress, contentLength, speed,id);
                                if (finalProgress == maxProgress) {
                                    fileCallback = null;
                                }
                            }
                        });

                    }
                }
                lastProgress = progress;

            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}