package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.UserRole;

import java.util.Map;

/**
 * 用户与角色关系表
 *
 * @author cj180
 * @date 2025-03-31
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
