package com.ryou.schoolcommunity.service.impl;

import com.ryou.schoolcommunity.entity.User;
import com.ryou.schoolcommunity.mapper.UserMapper;
import com.ryou.schoolcommunity.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryou.schoolcommunity.utils.TecentImUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ryou
 * @since 2022-03-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User wxRegister(Object wxOpenid, Object wxUnionid) {
        User user = new User();
        String openid = wxOpenid.toString();
        String unionid = wxUnionid != null ? wxUnionid.toString() : null;
        user.setNickname("wx_" + UUID.randomUUID().toString().substring(0,8));
        user.setWxOpenid(openid);
        user.setWxUnionid(unionid);
        boolean save = this.save(user);
        if (!save) {
            return null;
        }

        return user;
    }
}
