package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.Role;

import java.util.Map;

/**
 * 角色信息表
 *
 * @author cj180
 * @date 2025-04-01
 */
public interface RoleService extends IService<Role> {

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
