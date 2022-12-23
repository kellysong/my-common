package com.sjl.core.net.file;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface HttpFileApi {


    /**
     * 下载文件
     * <p>Range: bytes=0-    如省略第二个参数，即从索引开始位置，到结束位置</p>
     * <p>Range: bytes=-500  如省略地一个参数，即表示最后500个字符</p>
     * <p>Range: bytes=0-499 其实就是前500个字符</p>
     * <p>Range: bytes=2-10  第3个字符(索引位置为2)～第11个字符（索引位置为10）</p>
     * @param url   下载url
     * @param range Range表示断点续传的请求头参数
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url, @Header("Range") String range);


    /**
     * 上传文件
     *
     * @param url         上传url
     * @param requestBody 请求体
     * @return
     */
    @POST
    Observable<ResponseBody> upload(@Url String url, @Body RequestBody requestBody);
}
