package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.User;
import com.cayjin.backend.entity.vo.EmailRegisterVO;
import com.cayjin.backend.entity.vo.UserVO;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author cj180
 * @date 2025-03-31
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

    /**
     * 根据用户名或邮箱查询用户
     *
     * @param text 用户名或邮箱
     */
    UserVO getUserByNameOrEmail(String text);

    /**
     * 根据用户名或邮箱查询密码
     *
     * @param text 用户名或邮箱
     */
    String getPasswordByNameOrEmail(String text);

    /**
     * 发送注册或重置密码的验证码
     *
     * @param type 注册或重置密码
     * @param email 邮箱
     * @param ip 客户端IP
     */
    String registerEmailVerifyCode(String type, String email, String ip);

    /**
     * 注册用户
     *
     * @param emailRegisterVO 注册信息
     */
    String registerUserByEmail(EmailRegisterVO emailRegisterVO);

    /**
     * 校验邮箱
     *
     * @param emailRegisterVO 注册信息
     */
    String checkEmail(EmailRegisterVO emailRegisterVO);

    /**
     * 重置密码
     *
     * @param emailRegisterVO 注册信息
     */
    String resetPassword(EmailRegisterVO emailRegisterVO);

}
