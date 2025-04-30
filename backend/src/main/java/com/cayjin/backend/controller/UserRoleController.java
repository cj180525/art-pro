package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.UserRole;
import com.cayjin.backend.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 用户与角色关系表
 *
 * @author cj180
 * @date 2025-04-25
 */
@RestController
@RequestMapping("backend/userRole")
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = userRoleService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{userRoleId}")
    public RestBean<UserRole> info(@PathVariable("userRoleId") Long userRoleId) {
        UserRole userRole = userRoleService.getById(userRoleId);
        return RestBean.success(userRole);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody UserRole userRole) {
        userRoleService.save(userRole);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody UserRole userRole) {
        userRoleService.updateById(userRole);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] userRoleIds) {
        userRoleService.removeByIds(Arrays.asList(userRoleIds));
        return RestBean.success();
    }

}