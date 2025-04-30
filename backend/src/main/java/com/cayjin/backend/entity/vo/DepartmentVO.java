package com.cayjin.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门表视图
 *
 * @author cj180
 * @date 2025-03-25
 */
@Data
public class DepartmentVO {

    /**
     * 部门ID
     */
    private Integer id;
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
    /**
     * 子菜单
     */
    private List<DepartmentVO> children;

}