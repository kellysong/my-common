package com.sjl.core.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sjl.core.R;
import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.AppUtils;
import com.sjl.core.util.JsonUtils;
import com.sjl.core.util.log.LogUtils;
import com.sjl.core.util.log.LoggerUtils;
import com.sjl.core.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit帮助类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitHelper.java
 * @time 2018/1/29 15:09
 * @copyright(C) 2018 song
 */
public class RetrofitHelper {
    private volatile static RetrofitHelper sInstance;//使线程共享一个实例
    private Retrofit mRetrofit;

    private static final long CONNECT_TIMEOUT = 20L;
    private static final long READ_TIMEOUT = 20L;
    private static final long WRITE_TIMEOUT = 20L;
    //设缓存有效期为7天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 7;
    /**
     * 区分不同服务
     */
    private static final String DOMAIN_NAME = "Domain-Name";

    private BaseUrlAdapter mBaseUrlAdapter;
    private  static Map<String, String> mBaseUrlMap;



    private RetrofitHelper() {

    }

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化配置
     *
     * @param baseUrlAdapter
     */
    public void init(BaseUrlAdapter baseUrlAdapter) {
        if (baseUrlAdapter == null) {
            throw new NullPointerException("baseUrlAdapter is null");
        }
        this.mBaseUrlAdapter = baseUrlAdapter;
        this.mBaseUrlMap = mBaseUrlAdapter.getAppendBaseUrl();

        //Gson解析时间时的问题,gson默认可能无法识别这种字符串数字日期格式，需要写个转换适配器才行
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();

        mRetrofit = new Retrofit.Builder().client(getOkHttpClient())
                .baseUrl(mBaseUrlAdapter.getDefaultBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * baseurl拦截器,支持多个baseurl
     */
    private static final Interceptor mBaseUrlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取request
            Request request = chain.request();
            //获取request的创建者builder
            Request.Builder builder = request.newBuilder();
            //从request中获取headers，通过给定的键url_name
            List<String> headerValues = request.headers(DOMAIN_NAME);
            if ((headerValues != null && headerValues.size() > 0) && mBaseUrlMap != null && mBaseUrlMap.size() > 0) {
                //匹配获得新的BaseUrl
                HttpUrl newBaseUrl;
                String headerValue = headerValues.get(0);
                String baseUrl = mBaseUrlMap.get(headerValue);
                if (!TextUtils.isEmpty(baseUrl)) {
                    //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                    builder.removeHeader(DOMAIN_NAME);
                    newBaseUrl = HttpUrl.parse(baseUrl);
                } else {
                    return chain.proceed(request);
                }

                //从request中获取原有的HttpUrl实例oldHttpUrl
                HttpUrl oldHttpUrl = request.url();
                //重建新的HttpUrl，修改需要修改的url部分
                HttpUrl newFullUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(newBaseUrl.scheme())
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())
                        .build();
                //重建这个request，通过builder.url(newFullUrl).build()；
                //然后返回一个response至此结束修改
                return chain.proceed(builder.url(newFullUrl).build());
            } else {
                return chain.proceed(request);
            }
        }
    };


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
//            LogUtils.i("intercept url: "+request.url().toString());

            if (!AppUtils.isConnected(BaseApplication.getContext())) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(BaseApplication.getContext(), R.string.net_error, Toast.LENGTH_LONG);
                    }
                });

                LogUtils.i("从缓存读取数据");
                //设置缓存时间为1天

                CacheControl cacheControl = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale((int) CACHE_STALE_SEC, TimeUnit.SECONDS)
                        .build();
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }

            Response originalResponse = chain.proceed(request);
            if (AppUtils.isConnected(BaseApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(mBaseUrlInterceptor)
                .addInterceptor(mRewriteCacheControlInterceptor);
        //不开启日志拦截器，调试太卡
      /*  if (BuildConfig.DEBUG) {
            // 拦截okHttp的日志，如果开启了会导致上传回调被调用两次
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//必须设置级别，否则没有日志产生
            builder.addNetworkInterceptor(logInterceptor);
        }*/
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    /**
     * http日志拦截器
     * 将网络请求和响应相关数据通过日志的形式打印出来
     */
    private static final class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuffer mMessage = new StringBuffer();//由于网络请求是多线程，使用线程安全类,否则会数据下标越界

        @Override
        public void log(String message) {
            // 请求或者响应开始
            try {
                if (message.startsWith("--> POST")) {
                    mMessage.setLength(0);
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((message.startsWith("{") && message.endsWith("}"))
                        || (message.startsWith("[") && message.endsWith("]"))) {
                    message = JsonUtils.formatJson(JsonUtils.decodeUnicode(message));
                }
                mMessage.append(message.concat("\n"));
                // 响应结束，打印整条日志
                if (message.startsWith("<-- END HTTP")) {
                    LoggerUtils.d(mMessage.toString());
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }

        }
    }

    /**
     * 返回接口服务实例
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getApiService(Class<T> clz) {
        return mRetrofit.create(clz);
    }


}
