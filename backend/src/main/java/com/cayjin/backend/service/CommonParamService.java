package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.CommonParam;

import java.util.Map;

/**
 * 公共参数表
 *
 * @author cj180
 * @date 2025-04-28
 */
public interface CommonParamService extends IService<CommonParam> {

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
