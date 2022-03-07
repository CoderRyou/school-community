package com.ryou.schoolcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author ryou
 */
@MapperScan("com.ryou.schoolcommunity.mapper")
@SpringBootApplication
public class SchoolCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolCommunityApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
