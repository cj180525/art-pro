package com.cayjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.entity.ParamManage;
import com.cayjin.backend.entity.vo.ParamManageVO;

import java.util.List;
import java.util.Map;

/**
 * 参数管理表
 *
 * @author cj180
 * @date 2025-04-29
 */
public interface ParamManageService extends IService<ParamManage> {

    /**
     * 查询
     */
    List<ParamManageVO> queryByPaTable(String paTable);

    /**
     * 分页查询
     */
    BasePage queryPage(Map<String, Object> params);

}
