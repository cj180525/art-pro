package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 用户信息表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 性别 (1: 男, 2: 女)
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 部门ID
     */
    private Integer depId;
    /**
     * 状态 (1: 在线, 2: 离线, 3: 异常, 4: 注销)
     */
    private Integer status;

}