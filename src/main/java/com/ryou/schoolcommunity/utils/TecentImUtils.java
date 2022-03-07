package com.ryou.schoolcommunity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryou.schoolcommunity.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 腾讯IM相关工具类
 * @author ryou
 */
@Component
public class TecentImUtils {

    @Value("${tecent-im.sdkappid}")
    private String sdkappid;

    @Value("${tecent-im.identifier}")
    private String identifier;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private TLSSigAPIv2 tlsSigAPIv2;

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    private static final String SUCCESS_KEY = "ActionStatus";
    private static final String ERROR_INFO_KEY = "ErrorInfo";
    private static final String SUCCESS_VALUE = "OK";
    private static final String ACCOUNT_IMPORT_URL
            = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid={sdkappid}" +
            "&identifier={identifier}&usersig={usersig}&random={random}&contenttype=json";

    /**
     * 导入单个账号
     * @param user 用户对象
     * @param tClass 返回值类型
     * @param <T>
     * @return
     */
    public <T> T accountImport(User user, Class<T> tClass) {
        String userSig = tlsSigAPIv2.genUserSig(identifier, 60);
        HashMap<String, Object> params = getParams();
        params.put("usersig", userSig);
        HashMap request = new HashMap<String,Object>(4) {{
            put("UserID", user.getId().toString());
            put("Nick", user.getNickname());
            put("FaceUrl", user.getAvatarUrl());
        }};
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(
                    ACCOUNT_IMPORT_URL, entity, tClass, params);
    }

    /**
     * 判断请求是否成功的方法
     * @param response 请求返回对象
     * @return 是否成功
     */
    public boolean isRequestSuccess(Object response) {
        String value = getValueFromResponse(response, SUCCESS_KEY);
        if(value != null && value.equals(SUCCESS_VALUE)) {
            return true;
        }

        return false;
    }

    /**
     * 获取错误信息
     * @param response 请求返回对象
     * @return 错误信息字符串
     */
    public String getErrorInfo(Object response) {
        return getValueFromResponse(response, ERROR_INFO_KEY);
    }

    private String getValueFromResponse(Object response, String key) {
        if(response == null) {
            return null;
        }
        Object value = null;
        if(response instanceof Map) {
            Map map = (Map) response;
            value = map.get(key);
        } else {
            try {
                PropertyDescriptor pd =
                        new PropertyDescriptor(key, Object.class);
                Method rm = pd.getReadMethod();
                value = rm.invoke(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value.toString();
    }

    private HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("sdkappid", sdkappid);
        params.put("identifier", identifier);
        params.put("random", random.nextInt(Integer.MAX_VALUE));
        return params;
    }

}
