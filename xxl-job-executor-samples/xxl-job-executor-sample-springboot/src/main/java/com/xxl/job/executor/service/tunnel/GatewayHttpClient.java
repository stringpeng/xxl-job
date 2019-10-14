package com.xxl.job.executor.service.tunnel;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author StringPeng
 * Created on 2019/6/22
 */
public interface GatewayHttpClient {
    @RequestLine("POST {url}")
    @Headers({"Content-Type: text/xml","SOAPAction: {action}"})
    @Body("{data}")
    void execute(@Param("url")String url,
                 @Param("action") String action,
                 @Param("data") String data);
}