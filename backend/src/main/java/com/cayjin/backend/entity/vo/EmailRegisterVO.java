package com.cayjin.backend.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 邮箱注册请求参数视图
 *
 * @author cj180
 * @date 2025年4月25日
 */
@Data
public class EmailRegisterVO {

    /**
     * 邮箱
     * 1-50位，支持中英文、数字、下划线、@、.、-，不支持空格
     */
    @Email
    String email;

    /**
     * 验证码
     * 6位数字
     */
    @Length(min = 6, max = 6)
    String code;

    /**
     * 用户名
     * 1-20位，支持中英文、数字
     */
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 20)
    String username;

    /**
     * 密码
     * 6-20位，支持字母、数字、下划线
     */
    @Length(min = 6, max = 20)
    String password;
}
