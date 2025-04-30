package com.cayjin.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cayjin.backend.entity.User;
import com.cayjin.backend.entity.vo.UserDepVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author cj180
 * @date 2025-03-31
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<UserDepVO> getUsersWithDepName(IPage<UserDepVO> page, @Param("params") Map<String, Object> params);
}
