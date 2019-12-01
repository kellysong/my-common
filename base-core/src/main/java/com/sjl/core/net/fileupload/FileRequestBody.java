package com.sjl.core.net.fileupload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 自定义文件上传请求体,用于实现文件上传进度显示
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileRequestBody.java
 * @time 2018/8/13 17:31
 * @copyright(C) 2018 song
 */
public final class FileRequestBody<T> extends RequestBody {
    public static final int UPDATE = 0x01;

    /**
     * 实际请求体
     */
    private RequestBody requestBody;
    /**
     * 上传回调接口
     */
    private RetrofitCallback<T> callback;
    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;
    /**
     * 文件上传handler,用于进度更新
     */
    private FileUploadHandler fileUploadHandler;

    public FileRequestBody(RequestBody requestBody, RetrofitCallback<T> callback) {
        super();
        this.requestBody = requestBody;
        this.callback = callback;
        if (fileUploadHandler == null) {
            fileUploadHandler = new FileUploadHandler(this.callback);
        }
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        bufferedSink = Okio.buffer(sink(sink));

        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                Message msg = Message.obtain();
                msg.what = UPDATE;
                msg.obj = new FileInfo(bytesWritten, contentLength, bytesWritten == contentLength);
                fileUploadHandler.sendMessage(msg);
            }
        };
    }

    static class FileUploadHandler extends Handler {
        private WeakReference<RetrofitCallback> reference;

        public FileUploadHandler(RetrofitCallback retrofitCallback) {
            super(Looper.getMainLooper());
            reference = new WeakReference<RetrofitCallback>(retrofitCallback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    //回调
                    RetrofitCallback callback = reference.get();
                    if (callback != null) {
                        boolean done = fileInfo.isDone();
                        callback.onProgress(fileInfo.getCurrentBytes(), fileInfo.getContentLength(), done);
                        if (done){
                            //下载完毕
                           this.removeCallbacksAndMessages(null);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}