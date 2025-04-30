package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 公共参数表
 *
 * @author cj180
 * @date 2025-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_common_param")
public class CommonParam extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数表
     */
    private String paTable;
    /**
     * 参数名
     */
    private String paName;
    /**
     * 码
     */
    private String paKey;
    /**
     * 值
     */
    private String paValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 (1: 启用, 2: 禁用)
     */
    private Integer status;

}