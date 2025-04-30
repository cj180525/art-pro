package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.Menu;
import com.cayjin.backend.entity.vo.MenuVO;
import com.cayjin.backend.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单表
 *
 * @author cj180
 * @date 2025-03-31
 */
@RestController
@RequestMapping("api/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    /**
     * 列表
     */
    @GetMapping("/list/tree")
    public RestBean<List<MenuVO>> listTree() {
        List<MenuVO> list = menuService.listTree();
        return RestBean.success(list);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = menuService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{menuId}")
    public RestBean<Menu> info(@PathVariable("menuId") Long menuId) {
        Menu menu = menuService.getById(menuId);
        return RestBean.success(menu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody Menu menu) {
        menuService.save(menu);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] menuIds) {
        menuService.removeByIds(Arrays.asList(menuIds));
        return RestBean.success();
    }

}