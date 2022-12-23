package com.sjl.core.net;


import com.sjl.core.net.file.DownloadInfo;
import com.sjl.core.net.file.FileCallback;
import com.sjl.core.net.file.FileRequestBody;
import com.sjl.core.net.file.HttpFileApi;
import com.sjl.core.util.CollectionUtils;
import com.sjl.core.util.StopWatch;
import com.sjl.core.util.io.IOUtils;
import com.sjl.core.util.log.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feng.skin.manager.util.MapUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * 基于RxJava Http文件上传和下载封装
 *
 * @author Kelly
 * @version 1.0.0
 * @filename HttpFileUtils.java
 * @time 2018/7/25 16:04
 * @copyright(C) 2018 song
 */
public class RxHttpUtils {


    private volatile static RxHttpUtils rxHttpUtils;

    private Map<String, Disposable> tasks = null;

    private RxHttpUtils() {

    }

    public static RxHttpUtils getInstance() {
        if (rxHttpUtils == null) {
            synchronized (RxHttpUtils.class) {
                if (rxHttpUtils == null) {
                    rxHttpUtils = new RxHttpUtils();
                }
            }
        }
        return rxHttpUtils;
    }


    /**
     * 下载文件
     *
     * @param url            下载url
     * @param file           下载文件
     * @param downloadLength 已下载大小（断点不支持保存在数据库）,重新下载填0
     * @param fileCallback   下载回调
     */
    public void download(String url, final File file, int downloadLength, final FileCallback<File> fileCallback) {
        if (fileCallback == null) {
            throw new NullPointerException("fileCallBack 为空");
        }

        String range = "bytes=" + downloadLength + "-";
        HttpFileApi httpFileApi = RetrofitHelper.getInstance().getApiService(HttpFileApi.class);
        StopWatch stopWatch = new StopWatch("");
        stopWatch.start();
        Observable<ResponseBody> observable = httpFileApi.download(url, range);
        final DownloadInfo downloadInfo = new DownloadInfo();
        observable
                .flatMap(new Function<ResponseBody, ObservableSource<DownloadInfo>>() {

                    @Override
                    public ObservableSource<DownloadInfo> apply(final ResponseBody responseBody) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<DownloadInfo>() {
                            @Override
                            public void subscribe(ObservableEmitter<DownloadInfo> emitter) throws Exception {
                                stopWatch.stop();
                                LogUtils.i("下载响应耗时：" + stopWatch.prettyPrint());
                                InputStream inputStream = null;
                                long total = 0;
                                long contentLength;
                                RandomAccessFile randomAccessFile = null;
                                try {
                                    byte[] buf = new byte[8 * 1024];
                                    int len = 0;
                                    //每次返回剩余大小
                                    contentLength = responseBody.contentLength();
                                    inputStream = responseBody.byteStream();

                                    File dir = new File(file.getParent());
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    downloadInfo.setFile(file);
                                    downloadInfo.setFileSize(contentLength);
                                    randomAccessFile = new RandomAccessFile(file, "rwd");
                                    randomAccessFile.setLength(contentLength);
                                    long pos = downloadLength;
                                    randomAccessFile.seek(pos);
                                    int progress = 0;
                                    int lastProgress = -1;
                                    // 开始下载时获取开始时间
                                    long startTime = System.currentTimeMillis();
                                    while ((len = inputStream.read(buf)) != -1) {
                                        randomAccessFile.write(buf, 0, len);
                                        total += len;
                                        progress = (int) (total * 100 / contentLength);
                                        long curTime = System.currentTimeMillis();
                                        long usedTime = (curTime - startTime) / 1000;
                                        if (usedTime == 0) {
                                            usedTime = 1;
                                        }
                                        // 平均每秒下载速度
                                        long speed = (total / usedTime);
                                        // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                                        if (progress != lastProgress) {
                                            downloadInfo.setSpeed(speed);
                                            downloadInfo.setProgress(progress);
                                            downloadInfo.setCurrentSize(total);
                                            emitter.onNext(downloadInfo);
                                        }
                                        lastProgress = progress;
                                    }
                                    downloadInfo.setFile(file);
                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                } catch (Exception e) {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(e);
                                    }
                                } finally {
                                    IOUtils.close(randomAccessFile);
                                    IOUtils.close(inputStream);
                                }
                            }
                        });
                    }
                })
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(url, d);
                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        fileCallback.onProgress(downloadInfo.getProgress(), downloadInfo.getFileSize(), downloadInfo.getSpeed(), -1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fileCallback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fileCallback.onCompleted(downloadInfo.getFile());
                    }
                });
    }

    /**
     * 上传文件（支持多个文件上传）
     *
     * @param url          上传url
     * @param files        上传文件
     * @param fileNames    上传文件name，对应服务器接收的name
     * @param extraParams  额外参数
     * @param fileCallback 上传回调
     */
    public void upload(String url, List<File> files, List<String> fileNames, Map<String, String> extraParams, final FileCallback<List<File>> fileCallback) {
        if (fileCallback == null) {
            throw new NullPointerException("fileCallBack 为空");
        }
        if (CollectionUtils.isEmpty(files)) {
            throw new NullPointerException("files 为空");
        }

        if (CollectionUtils.isEmpty(fileNames)) {
            throw new NullPointerException("fileNames 为空");
        }
        if (files.size() != fileNames.size()) {
            throw new IllegalArgumentException("files大小和fileNames大小不一致");
        }
        HttpFileApi apiService = RetrofitHelper.getInstance().getApiService(HttpFileApi.class);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String name = fileNames.get(i);
            FileRequestBody fileRequestBody = new FileRequestBody(i, file, "application/octet-stream", fileCallback);
            builder.addFormDataPart(name, file.getName(), fileRequestBody);
        }
        if (MapUtils.isNotEmpty(extraParams)) {
            for (Map.Entry<String, String> entry : extraParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addFormDataPart(key, value);
            }
        }


        final MultipartBody requestBody = builder.build();
        Observable<ResponseBody> upload = apiService.upload(url, requestBody);

        Disposable disposable = upload.compose(RxSchedulers.applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (!isExist(url)) {
                            return;
                        }
                        fileCallback.onCompleted(files);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isExist(url)) {
                            return;
                        }
                        fileCallback.onError(throwable);
                    }
                });
        addDisposable(url, disposable);
    }

    /**
     * 取消单个任务
     *
     * @param url
     */
    public void cancel(String url) {
        if (tasks != null) {
            Disposable disposable = tasks.get(url);
            if (disposable != null) {
                disposable.dispose();
            }
        }
    }

    /**
     * 任务是否存在
     *
     * @param url
     * @return
     */
    public boolean isExist(String url) {
        if (tasks != null) {
            Disposable disposable = tasks.get(url);
            if (disposable != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 清空所有任务
     */
    public void clear() {
        if (tasks != null) {
            for (Map.Entry<String, Disposable> entry : tasks.entrySet()) {
                Disposable value = entry.getValue();
                value.dispose();
            }
            tasks.clear();
        }
    }

    private synchronized void addDisposable(String url, Disposable disposable) {
        if (tasks == null) {
            tasks = new HashMap<>();
        }
        tasks.put(url, disposable);
    }
}
