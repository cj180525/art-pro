package com.cayjin.backend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单信息视图
 *
 * @author cj180
 * @date 2025-03-17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 路径
     */
    private String path;
    /**
     * 名称
     */
    private String name;
    /**
     * 组件
     */
    private String component;

    /**
     * 菜单类型 (1:目录，2:页面，3:内嵌，4:外链)
     */
    private Integer type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否显示徽章
     */
    private Boolean showBadge;
    /**
     * 徽章文本
     */
    private String showTextBadge;
    /**
     * 是否隐藏
     */
    private Boolean isHide;
    /**
     * 是否隐藏标签
     */
    private Boolean isHideTab;
    /**
     * 链接
     */
    private String link;
    /**
     * 是否为Iframe
     */
    private Boolean isIframe;
    /**
     * 是否保持活跃
     */
    private Boolean keepAlive;
    /**
     * 权限列表
     */
    private String authList;
    /**
     * 是否在主容器中
     */
    private Boolean isInMainContainer;

    /**
     * 子菜单
     */
    private List<MenuVO> children;

}