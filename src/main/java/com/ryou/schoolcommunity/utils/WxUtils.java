package com.ryou.schoolcommunity.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author ryou
 */
@Component
public class WxUtils {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ObjectMapper objectMapper;

    private static final String CODE2SESSION_URL
            = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.app-secret}")
    private String appSecret;

    public <T> T code2Session(String jsCode,Class<T> tClass) {
        HashMap<String, Object> params = new HashMap(4) {
            {
                put("appid", appid);
                put("secret", appSecret);
                put("js_code", jsCode);
            }
        };
        System.out.println(params);
        String forObject = restTemplate.getForObject(CODE2SESSION_URL, String.class, params);
        try {
            return objectMapper.readValue(forObject,tClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
