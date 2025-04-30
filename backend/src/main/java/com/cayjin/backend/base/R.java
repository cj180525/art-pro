package com.cayjin.backend.base;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.cayjin.backend.common.ResponseCode;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据(切换为RestBean)
 *
 * @author cj180
 * @date 2025年3月31日
 */
public class R extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", ResponseCode.HTTP_SUCCESS.getCode());
        put("msg", ResponseCode.HTTP_SUCCESS.getMsg());
    }

    public static R error() {
        return error(ResponseCode.UNKNOWN_EXCEPTION);
    }

    public static R error(String msg) {
        return error(ResponseCode.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(ResponseCode responseCode) {
        return error(responseCode.getCode(), responseCode.getMsg());
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
