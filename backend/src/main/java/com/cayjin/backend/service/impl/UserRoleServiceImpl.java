package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.UserRole;
import com.cayjin.backend.mapper.UserRoleMapper;
import com.cayjin.backend.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户与角色关系表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        IPage<UserRole> page = this.page(
                new BaseQuery<UserRole>().getPage(params),
                new QueryWrapper<>()
        );
        return new BasePage(page);
    }

}
