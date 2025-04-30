package com.cayjin.backend.entity.vo;

import lombok.Data;

/**
 * 用户信息表视图
 *
 * @author cj180
 * @date 2025-03-31
 */
@Data
public class UserVO {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别 (1: 男, 2: 女)
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;

}