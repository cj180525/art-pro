package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 参数管理表
 *
 * @author cj180
 * @date 2025-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_param_manage")
public class ParamManage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 表
     */
    private String paTable;
    /**
     * 字段
     */
    private String paField;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段长度
     */
    private Integer fieldLength;
    /**
     * 字段类型(1:input,2:select,3:timestamp,4:textarea)
     */
    private Integer fieldType;
    /**
     * 是否必输(1:是, 2: 否)
     */
    private Integer required;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态(1:启用, 2:禁用)
     */
    private Integer status;

}