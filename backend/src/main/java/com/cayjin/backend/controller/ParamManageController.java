package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.ParamManage;
import com.cayjin.backend.entity.vo.ParamManageVO;
import com.cayjin.backend.service.ParamManageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 参数管理表
 *
 * @author cj180
 * @date 2025-04-29
 */
@RestController
@RequestMapping("api/paramManage")
public class ParamManageController {
    @Resource
    private ParamManageService paramManageService;

    /**
     * 列表
     */
    @GetMapping("/queryByPaTable")
    public RestBean<List<ParamManageVO>> queryByPaTable(@RequestParam String paTable) {
        List<ParamManageVO> list = paramManageService.queryByPaTable(paTable);
        return RestBean.success(list);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = paramManageService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{paramManageId}")
    public RestBean<ParamManage> info(@PathVariable("paramManageId") Long paramManageId) {
        ParamManage paramManage = paramManageService.getById(paramManageId);
        return RestBean.success(paramManage);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody ParamManage paramManage) {
        paramManageService.save(paramManage);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody ParamManage paramManage) {
        paramManageService.updateById(paramManage);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] paramManageIds) {
        paramManageService.removeByIds(Arrays.asList(paramManageIds));
        return RestBean.success();
    }

}