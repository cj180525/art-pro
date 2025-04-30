package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.entity.CommonParam;
import com.cayjin.backend.entity.ParamManage;
import com.cayjin.backend.entity.vo.ParamManageVO;
import com.cayjin.backend.mapper.ParamManageMapper;
import com.cayjin.backend.service.ParamManageService;
import com.google.common.base.CaseFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参数管理表
 *
 * @author cj180
 * @date 2025-04-29
 */
@Service
public class ParamManageServiceImpl extends ServiceImpl<ParamManageMapper, ParamManage> implements ParamManageService {

    @Override
    public List<ParamManageVO> queryByPaTable(String paTable) {
        List<ParamManage> list = this.list(
                new QueryWrapper<ParamManage>()
                        .eq("pa_table", paTable)
                        .eq("status", 1)
                        .eq("is_deleted", 0)
        );
        return list.stream().map(item -> {
            ParamManageVO paramManageVO = new ParamManageVO();
            BeanUtils.copyProperties(item, paramManageVO);
            // 下划线转驼峰
            String newField = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, paramManageVO.getPaField());
            paramManageVO.setPaField(newField);
            return paramManageVO;
        }).collect(Collectors.toList());


    }

    @Override
    public BasePage queryPage(Map<String, Object> params) {
        QueryWrapper<ParamManage> wrapper = new QueryWrapper<>();

        if (params.get("paTable") != null && !params.get("paTable").toString().isEmpty()) {
            wrapper.like("pa_table", params.get("paTable"));
        }
        if (params.get("paField") != null && !params.get("paField").toString().isEmpty()) {
            wrapper.like("pa_field", params.get("paField"));
        }
        if (params.get("fieldName") != null && !params.get("fieldName").toString().isEmpty()) {
            wrapper.like("field_name", params.get("fieldName"));
        }
        if (params.get("fieldLength") != null && !params.get("fieldLength").toString().isEmpty()) {
            wrapper.like("field_length", params.get("fieldLength"));
        }
        if (params.get("fieldType") != null && !params.get("fieldType").toString().isEmpty()) {
            wrapper.eq("field_type", params.get("fieldType"));
        }
        if (params.get("required") != null && !params.get("required").toString().isEmpty()) {
            wrapper.eq("required", params.get("required"));
        }
        if (params.get("sort") != null && !params.get("sort").toString().isEmpty()) {
            wrapper.eq("sort", params.get("sort"));
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

        IPage<ParamManage> page = this.page(
                new BaseQuery<ParamManage>().getPage(params),
                wrapper
        );

        return new BasePage(page);
    }

}
