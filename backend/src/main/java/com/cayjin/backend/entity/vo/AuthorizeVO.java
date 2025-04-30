package com.cayjin.backend.entity.vo;

import lombok.Data;

/**
 * 登录返回数据
 *
 * @author cj180
 * @date 2025年4月25日
 */
@Data
public class AuthorizeVO {

    /**
     * jwt token
     */
    String token;

    /**
     * 过期时间(天)
     */
    Integer expire;

    /**
     * 用户信息
     */
    UserVO user;
}
