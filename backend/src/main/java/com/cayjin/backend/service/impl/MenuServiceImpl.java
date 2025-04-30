package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.Menu;
import com.cayjin.backend.entity.vo.MenuVO;
import com.cayjin.backend.mapper.MenuMapper;
import com.cayjin.backend.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 系统菜单表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();

        // 查询条件
        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            wrapper.like("name", params.get("name"));
        }
        if (params.get("type") != null && !params.get("type").toString().isEmpty()) {
            wrapper.eq("type", params.get("type"));
        }
        if (params.get("path") != null && !params.get("path").toString().isEmpty()) {
            wrapper.like("path", params.get("path"));
        }
        if (params.get("component") != null && !params.get("component").toString().isEmpty()) {
            wrapper.like("component", params.get("component"));
        }
        if (params.get("status") != null && !params.get("status").toString().isEmpty()) {
            wrapper.eq("status", params.get("status"));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (params.get("startTime") != null && !params.get("startTime").toString().isEmpty()) {
            LocalDateTime startTime = LocalDateTime.parse((String) params.get("startTime"), formatter);
            wrapper.ge("create_time", startTime);
        }
        if (params.get("endTime") != null && !params.get("endTime").toString().isEmpty()) {
            LocalDateTime endTime = LocalDateTime.parse((String) params.get("endTime"), formatter);
            wrapper.le("create_time", endTime);
        }

        IPage<Menu> page = this.page(new BaseQuery<Menu>().getPage(params), wrapper);
        return new BasePage(page);
    }

    @Override
    public List<MenuVO> listTree() {
        // 查询所有菜单项
        List<Menu> menus = baseMapper.selectList(new QueryWrapper<>());

        // 构建树结构
        return createTree(menus, null);
    }

    private List<MenuVO> createTree(List<Menu> menus, Integer parentId) {
        return menus.stream().filter(menu -> Objects.equals(menu.getParentId(), parentId)).map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            menuVO.setChildren(createTree(menus, menu.getId()));
            return menuVO;
        }).toList();
    }


}
