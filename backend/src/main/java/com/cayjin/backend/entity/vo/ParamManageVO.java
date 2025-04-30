package com.cayjin.backend.entity.vo;

import lombok.Data;

/**
 * 参数管理视图
 *
 * @author cj180
 * @date 2025-04-29
 */
@Data
public class ParamManageVO {

    /**
     * 字段
     */
    private String paField;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段类型(1:input,2:select,3:timestamp,4:textarea)
     */
    private Integer fieldType;
    /**
     * 是否必输(1:是, 2: 否)
     */
    private Integer required;

}