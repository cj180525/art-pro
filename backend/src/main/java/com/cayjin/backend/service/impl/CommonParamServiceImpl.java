package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.CommonParam;
import com.cayjin.backend.entity.Role;
import com.cayjin.backend.mapper.CommonParamMapper;
import com.cayjin.backend.service.CommonParamService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 公共参数表
 *
 * @author cj180
 * @date 2025-04-28
 */
@Service
public class CommonParamServiceImpl extends ServiceImpl<CommonParamMapper, CommonParam> implements CommonParamService {

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        QueryWrapper<CommonParam> wrapper = new QueryWrapper<>();

        if (params.get("paTable") != null && !params.get("paTable").toString().isEmpty()) {
            wrapper.like("pa_table", params.get("paTable"));
        }
        if (params.get("paName") != null && !params.get("paName").toString().isEmpty()) {
            wrapper.like("pa_name", params.get("paName"));
        }
        if (params.get("paKey") != null && !params.get("paKey").toString().isEmpty()) {
            wrapper.like("pa_key", params.get("paKey"));
        }
        if (params.get("paValue") != null && !params.get("paValue").toString().isEmpty()) {
            wrapper.like("pa_value", params.get("paValue"));
        }
        if (params.get("remark") != null && !params.get("remark").toString().isEmpty()) {
            wrapper.like("remark", params.get("remark"));
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

        IPage<CommonParam> page = this.page(
                new BaseQuery<CommonParam>().getPage(params),
                wrapper
        );
        return new BasePage(page);
    }

}
