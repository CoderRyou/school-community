package com.ryou.schoolcommunity.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ryou
 * @since 2022-03-01
 */
@Data
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户微信小程序唯一标识
     */
    private String wxOpenid;

    /**
     * 用户开放平台唯一标识
     */
    private String wxUnionid;

    /**
     * 微信头像
     */
    private String avatarUrl;

    /**
     * 性别（0未知 1男 2女）
     */
    private Integer gender;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 腾讯IM账号是否创建
     */
    private Boolean imCreated;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 所属学校id
     */
    private Integer schoolId;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    @JsonIgnore
    private Integer version;
}
