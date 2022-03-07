package com.ryou.schoolcommunity.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryou.schoolcommunity.entity.User;
import com.ryou.schoolcommunity.pojo.vo.UserPerfectVO;
import com.ryou.schoolcommunity.service.IUserService;
import com.ryou.schoolcommunity.utils.MsgConst;
import com.ryou.schoolcommunity.utils.TLSSigAPIv2;
import com.ryou.schoolcommunity.utils.TecentImUtils;
import com.ryou.schoolcommunity.utils.WxUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ryou
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private WxUtils wxUtils;

    @Resource
    private TLSSigAPIv2 tlsSigAPIv2;

    @Resource
    private TecentImUtils tecentImUtils;

    @PostMapping("/wx-login")
    public ResponseEntity login(@RequestParam String code) {
        if(StrUtil.isBlank(code)) {
            return ResponseEntity.badRequest()
                    .body("code不能为空");
        }
        HashMap res = wxUtils.code2Session(code,HashMap.class);
        if(res.get("errcode") != null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("登录失败，详情：" + res.get("errmsg"));
        }
        Object openid = res.get("openid");
        Object unionid = res.get("unionid");
        boolean isFirst = false;
        User user = userService.getOne(new QueryWrapper<User>()
                .eq("wx_openid", openid.toString()));
        if(user == null) {
            isFirst = true;
            user = userService.wxRegister(openid, unionid);
            if(user == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("登录失败，详情：用户保存失败");
            }
            user = userService.getById(user.getId());
        }
        StpUtil.login(user.getId());
        if(isFirst || !user.getImCreated()) {
            // 导入腾讯IM账号
            HashMap importRes = tecentImUtils.accountImport(user, HashMap.class);
            if(tecentImUtils.isRequestSuccess(importRes)) {
                User user1 = new User();
                user1.setId(user.getId());
                user1.setImCreated(true);
                userService.updateById(user1);
            }
        }

        HashMap req = new HashMap(2);
        req.put("first", isFirst);
        req.put("imUserSig", tlsSigAPIv2.genUserSig(user.getId().toString()));
        req.put("user", user);
        return ResponseEntity.ok(req);
    }

    @PostMapping("perfect")
    @Validated
    public ResponseEntity perfectUserInfo(UserPerfectVO vo) {
        User user = BeanUtil.copyProperties(vo, User.class);
        boolean b = userService.updateById(user);
        if(b) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body(MsgConst.UPDATE_ERROR);
    }

}
