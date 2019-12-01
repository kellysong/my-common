package com.sjl.core.net;

import java.util.Map;

/**
 * BaserUrl适配器，使Retrofit支持多baseUrl
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseUrlAdapter.java
 * @time 2019/1/10 17:50
 * @copyright(C) 2019 song
 */
public interface  BaseUrlAdapter {
    /**
     * 获取默认的baserUrl
     * @return
     */
    String getDefaultBaseUrl();

    /**
     *获取otherBaseUrl
     * @return Domain-Name和服务base url的映射
     */
    Map<String,String> getAppendBaseUrl();
}
