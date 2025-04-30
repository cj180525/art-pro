package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.Role;
import com.cayjin.backend.mapper.RoleMapper;
import com.cayjin.backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 角色信息表
 *
 * @author cj180
 * @date 2025-04-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();

        if (params.get("roleName") != null && !params.get("roleName").toString().isEmpty()) {
            wrapper.like("role_name", params.get("roleName"));
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

        IPage<Role> page = this.page(
                new BaseQuery<Role>().getPage(params),
                wrapper
        );
        return new BasePage(page);
    }

}
