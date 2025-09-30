package com.example.demo.config;


import java.util.List;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            // globalNo만 수동으로 넣어줌
            String globalNo = MDC.get("globalNo");
            if (globalNo != null) {
                request.getHeaders().add("X-Global-No", globalNo);
            }

            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
