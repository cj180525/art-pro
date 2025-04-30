package com.cayjin.backend.base;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.cayjin.backend.common.ResponseCode;

/**
 * 返回数据
 *
 * @author cj180
 * @date 2025年3月31日
 */
public record RestBean<T>(int code,  String msg, T data) {

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(ResponseCode.HTTP_SUCCESS.getCode(),ResponseCode.HTTP_SUCCESS.getMsg(), data);
    }

    public static <T> RestBean<T> failure(int code, String msg) {
        return new RestBean<>(code, msg, null);
    }

    public static <T> RestBean<T> failure(ResponseCode responseCode) {
        return failure(responseCode.getCode(), responseCode.getMsg());
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
