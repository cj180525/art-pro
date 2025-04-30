package com.cayjin.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cayjin.backend.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统菜单表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pa_menu")
public class Menu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
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
     * 父菜单ID
     */
    private Integer parentId;

}