package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 部门表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_department")
public class Department extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    private String depName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态 (1: 启用, 2: 禁用)
     */
    private Integer status;
    /**
     * 父部门ID
     */
    private Integer parentId;

}