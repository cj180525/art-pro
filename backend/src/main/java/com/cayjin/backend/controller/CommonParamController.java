package com.cayjin.backend.controller;

import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.entity.CommonParam;
import com.cayjin.backend.service.CommonParamService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 公共参数表
 *
 * @author cj180
 * @date 2025-04-28
 */
@RestController
@RequestMapping("api/commonParam")
public class CommonParamController {
    @Resource
    private CommonParamService commonParamService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public RestBean<BasePage> list(@RequestParam Map<String, Object> params) {
        BasePage page = commonParamService.queryPage(params);
        return RestBean.success(page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{commonParamId}")
    public RestBean<CommonParam> info(@PathVariable("commonParamId") Long commonParamId) {
        CommonParam commonParam = commonParamService.getById(commonParamId);
        return RestBean.success(commonParam);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public RestBean<Void> save(@RequestBody CommonParam commonParam) {
        commonParamService.save(commonParam);
        return RestBean.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public RestBean<Void> update(@RequestBody CommonParam commonParam) {
        commonParamService.updateById(commonParam);
        return RestBean.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public RestBean<Void> delete(@RequestBody Long[] commonParamIds) {
        commonParamService.removeByIds(Arrays.asList(commonParamIds));
        return RestBean.success();
    }

}