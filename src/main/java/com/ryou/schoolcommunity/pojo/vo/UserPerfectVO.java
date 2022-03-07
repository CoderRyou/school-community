package com.ryou.schoolcommunity.pojo.vo;

import com.sun.istack.internal.NotNull;
import lombok.Data;

/**
 * @author ryou
 */
@Data
public class UserPerfectVO {

    private Integer id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    @NotNull
    private String avatarUrl;

    /**
     * 性别（0未知 1男 2女）
     */
    private Integer gender;

    /**
     * 电话号码
     */
    @NotNull
    private String phoneNumber;

    /**
     * 真实姓名
     */
    @NotNull
    private String realName;

}
