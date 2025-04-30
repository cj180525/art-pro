package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.Department;
import com.cayjin.backend.entity.vo.DepartmentVO;
import com.cayjin.backend.mapper.DepartmentMapper;
import com.cayjin.backend.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 部门表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        IPage<Department> page = this.page(
                new BaseQuery<Department>().getPage(params),
                new QueryWrapper<>()
        );
        return new BasePage(page);
    }


    @Override
    public List<DepartmentVO> listTree(Map<String, Object> params) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();

        // 查询条件
        if (params.get("depName") != null && !params.get("depName").toString().isEmpty()) {
            wrapper.like("dep_name", params.get("depName"));
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

        // 查询所有菜单项
        List<Department> departments = baseMapper.selectList(wrapper);

        // 构建树结构
        return createTree(departments, null);
    }

    private List<DepartmentVO> createTree(List<Department> departments, Integer parentId) {
        return departments
                .stream()
                .filter(department -> Objects.equals(department.getParentId(), parentId))
                .map(department -> {
                    DepartmentVO departmentVO = new DepartmentVO();
                    BeanUtils.copyProperties(department, departmentVO);
                    departmentVO.setChildren(createTree(departments, department.getId()));
                    return departmentVO;
                })
                .toList();
    }

}
