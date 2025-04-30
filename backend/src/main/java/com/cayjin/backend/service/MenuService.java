package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.Menu;
import com.cayjin.backend.entity.vo.MenuVO;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单表
 *
 * @author cj180
 * @date 2025-03-31
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询返回树结构
     */
    List<MenuVO> listTree();

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
