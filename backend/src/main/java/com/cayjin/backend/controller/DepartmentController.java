package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.Department;
import com.cayjin.backend.entity.vo.DepartmentVO;
import com.cayjin.backend.service.DepartmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 部门表
 *
 * @author cj180
 * @date 2025-03-31
 */
@RestController
@RequestMapping("api/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    /**
     * 列表
     */
    @GetMapping("/list/tree")
    public RestBean<List<DepartmentVO>> listTree(@RequestParam Map<String, Object> params) {
        List<DepartmentVO> list = departmentService.listTree(params);
        return RestBean.success(list);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = departmentService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{departmentId}")
    public RestBean<Department> info(@PathVariable("departmentId") Long departmentId) {
        Department department = departmentService.getById(departmentId);
        return RestBean.success(department);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody Department department) {
        departmentService.save(department);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] departmentIds) {
        departmentService.removeByIds(Arrays.asList(departmentIds));
        return RestBean.success();
    }

}