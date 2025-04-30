package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色信息表
 *
 * @author cj180
 * @date 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_role")
public class Role extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String roleDesc;
    /**
     * 状态 (1: 启用, 2: 禁用)
     */
    private Integer status;

}