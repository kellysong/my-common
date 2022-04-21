package com.sjl.core.net;


import com.sjl.core.util.JsonUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * 修复网络日志拦截器打印日志错乱问题
 *
 * @author Kelly
 * @version 1.0.0
 * @filename NetLogInterceptor
 * @time 2022/4/15 14:51
 * @copyright(C) 2022 song
 */
public final class HttpLoggingInterceptor2 implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /** No logs. */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        /**
         * 日志回调
         * @param message 返回一个完整的日志信息
         */
        void log(String message);
    }
    private static final String NEW_LINE="\n";
    private  void callbackLog(StringBuilder sb) {
        logger.log(sb.toString());
        sb.setLength(0);
    }

    public HttpLoggingInterceptor2(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /** Change the level at which this interceptor logs. */
    public HttpLoggingInterceptor2 setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(requestStartMessage).append(NEW_LINE);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    sb.append("Content-Type: " + requestBody.contentType()).append(NEW_LINE);
                }
                if (requestBody.contentLength() != -1) {
                    sb.append("Content-Length: " + requestBody.contentLength()).append(NEW_LINE);
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    sb.append(name + ": " + headers.value(i)).append(NEW_LINE);
                }
            }

            if (!logBody || !hasRequestBody) {
                sb.append("--> END " + request.method()).append(NEW_LINE);
            } else if (bodyHasUnknownEncoding(request.headers())) {
                sb.append("--> END " + request.method() + " (encoded body omitted)").append(NEW_LINE);
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                sb.append("");
                if (isPlaintext(buffer)) {
                    String s = JsonUtils.formatJson(buffer.readString(charset));
                    sb.append(s).append(NEW_LINE);
                    sb.append("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)").append(NEW_LINE);
                } else {
                    sb.append("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)").append(NEW_LINE);
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            sb.append("<-- HTTP FAILED: " + e).append(NEW_LINE);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        sb.append("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')').append(NEW_LINE);

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                sb.append(headers.name(i) + ": " + headers.value(i)).append(NEW_LINE);
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                sb.append("<-- END HTTP").append(NEW_LINE);;
                callbackLog(sb);
            } else if (bodyHasUnknownEncoding(response.headers())) {
                sb.append("<-- END HTTP (encoded body omitted)").append(NEW_LINE);
                callbackLog(sb);
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    GzipSource gzippedResponseBody = null;
                    try {
                        gzippedResponseBody = new GzipSource(buffer.clone());
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    } finally {
                        if (gzippedResponseBody != null) {
                            gzippedResponseBody.close();
                        }
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    sb.append("").append(NEW_LINE);
                    sb.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)").append(NEW_LINE);
                    callbackLog(sb);
                    return response;
                }

                if (contentLength != 0) {
                    sb.append("").append(NEW_LINE);
                    String s = buffer.clone().readString(charset);
                    sb.append(JsonUtils.formatJson(s)).append(NEW_LINE);
                }

                if (gzippedLength != null) {
                    sb.append("<-- END HTTP (" + buffer.size() + "-byte, "
                            + gzippedLength + "-gzipped-byte body)").append(NEW_LINE);
                } else {
                    sb.append("<-- END HTTP (" + buffer.size() + "-byte body)").append(NEW_LINE);
                }
                callbackLog(sb);
            }
        }

        return response;
    }


    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}

