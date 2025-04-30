package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.Department;
import com.cayjin.backend.entity.vo.DepartmentVO;

import java.util.List;
import java.util.Map;

/**
 * 部门表
 *
 * @author cj180
 * @date 2025-03-31
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 分页查询
     */
    List<DepartmentVO> listTree(Map<String, Object> params);

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
