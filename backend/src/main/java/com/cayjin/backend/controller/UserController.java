package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.User;
import com.cayjin.backend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 用户信息表
 *
 * @author cj180
 * @date 2025-03-31
 */
@RestController
@RequestMapping("api/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = userService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{userId}")
    public RestBean<User> info(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        return RestBean.success(user);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody User user) {
        userService.save(user);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody User user) {
        userService.updateById(user);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] userIds) {
        userService.removeByIds(Arrays.asList(userIds));
        return RestBean.success();
    }

}