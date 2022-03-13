package com.magic.time.service.business_development_100.pool_04;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-25 5:33 PM
 **/
@Configuration
public class RestConfig {

    @Bean
        public RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(6500)
                    .setConnectTimeout(6500)
                    //从 connection manager中获取连接超时时间，没有可用连接，就会抛出ConnectionPoolTimeoutException异常。默认
                    .setConnectionRequestTimeout(10000)
                    .build();

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(400);
            connectionManager.setDefaultMaxPerRoute(150);

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .evictIdleConnections(60, TimeUnit.SECONDS).build();

          /*  RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            return restTemplate;*/

        /**
         * 使用Spring Boot的 MappingJackson2HttpMessageConverter，来使yml中Jackson的配置项生效
         */
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
             restTemplate.getMessageConverters().set(6, mappingJackson2HttpMessageConverter);
            return restTemplate;
        }
}
