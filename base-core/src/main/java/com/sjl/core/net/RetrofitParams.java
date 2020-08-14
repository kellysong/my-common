package com.sjl.core.net;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitParams.java
 * @time 2019/12/6 10:19
 * @copyright(C) 2019 song
 */
public class RetrofitParams {

    /**
     * base url适配器
     */
    private BaseUrlAdapter baseUrlAdapter;
    /**
     * log适配器
     */
    private RetrofitLogAdapter retrofitLogAdapter;
    /**
     * 拦截器
     */
    private List<Interceptor> interceptorList;

    private int connectTimeout = 30;
    private int readTimeout = 30;
    private int writeTimeout = 30;

    public RetrofitParams(Builder builder) {
        this.baseUrlAdapter = builder.baseUrlAdapter;
        this.retrofitLogAdapter = builder.retrofitLogAdapter;
        this.interceptorList = builder.interceptorList;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }


    public static final class Builder {
        private BaseUrlAdapter baseUrlAdapter;
        private RetrofitLogAdapter retrofitLogAdapter;
        private List<Interceptor> interceptorList = new ArrayList<>();
        private int connectTimeout;
        private int readTimeout;
        private int writeTimeout;

        public Builder setBaseUrlAdapter(BaseUrlAdapter baseUrlAdapter) {
            if (baseUrlAdapter == null) {
                throw new NullPointerException("baseUrlAdapter is null");
            }
            this.baseUrlAdapter = baseUrlAdapter;
            return this;
        }

        public Builder setRetrofitLogAdapter(RetrofitLogAdapter retrofitLogAdapter) {
            if (retrofitLogAdapter == null) {
                throw new NullPointerException("retrofitLogAdapter is null");
            }
            this.retrofitLogAdapter = retrofitLogAdapter;
            return this;
        }

        public Builder setInterceptor(Interceptor interceptor) {
            if (interceptor != null) {
                interceptorList.add(interceptor);
            }
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            if (connectTimeout < 0) {
                throw new IllegalArgumentException("connectTimeout cannot be zero.");
            }
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            if (readTimeout < 0) {
                throw new IllegalArgumentException("readTimeout cannot be zero.");
            }
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(int writeTimeout) {
            if (writeTimeout < 0) {
                throw new IllegalArgumentException("writeTimeout cannot be zero.");
            }
            this.writeTimeout = writeTimeout;
            return this;
        }

        public RetrofitParams build() {
            return new RetrofitParams(this);
        }
    }

    public BaseUrlAdapter getBaseUrlAdapter() {
        return baseUrlAdapter;
    }

    public RetrofitLogAdapter getRetrofitLogAdapter() {
        return retrofitLogAdapter;
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }
}
