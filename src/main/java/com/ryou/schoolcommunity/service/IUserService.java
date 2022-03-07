package com.ryou.schoolcommunity.service;

import com.ryou.schoolcommunity.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ryou
 * @since 2022-03-01
 */
public interface IUserService extends IService<User> {

    /**
     * 微信登录注册账号
     * @param wxOpenid
     * @param wxUnionid
     * @param imUserSig
     * @return
     */
    public User wxRegister(Object wxOpenid, Object wxUnionid);

}
