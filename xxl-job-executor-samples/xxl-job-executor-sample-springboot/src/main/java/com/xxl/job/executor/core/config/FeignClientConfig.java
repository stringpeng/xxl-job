package com.xxl.job.executor.core.config;

import com.xxl.job.executor.service.tunnel.GatewayHttpClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author StringPeng
 * Created on 2019/6/22
 */
@Configuration
public class FeignClientConfig {

    @Value("${feign.service.gateway.host}")
    private String host;

    @Bean
    GatewayHttpClient gatewayHttpClient() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger(GatewayHttpClient.class))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
//                .requestInterceptor(im163CloudInterceptor)
                .target(GatewayHttpClient.class, host);
    }
}