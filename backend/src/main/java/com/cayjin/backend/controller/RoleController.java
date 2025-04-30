package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.Role;
import com.cayjin.backend.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 角色信息表
 *
 * @author cj180
 * @date 2025-04-01
 */
@RestController
@RequestMapping("api/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = roleService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{roleId}")
    public RestBean<Role> info(@PathVariable("roleId") Long roleId) {
        Role role = roleService.getById(roleId);
        return RestBean.success(role);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody Role role) {
        roleService.save(role);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody Role role) {
        roleService.updateById(role);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] roleIds) {
        roleService.removeByIds(Arrays.asList(roleIds));
        return RestBean.success();
    }

}