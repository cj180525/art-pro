package com.cayjin.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 带部门名称的用户视图
 *
 * @author cj180
 * @date 2025年4月1日
 */
@Data
public class UserDepVO {

    /**
     * 主键ID
     */
    private Integer id;
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
     * 部门ID
     */
    private String depName;
    /**
     * 状态 (1: 在线, 2: 离线, 3: 异常, 4: 注销)
     */
    private Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
