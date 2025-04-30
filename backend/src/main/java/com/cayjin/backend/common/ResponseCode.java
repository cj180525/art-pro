package com.cayjin.backend.common;

import lombok.Getter;

/**
 * 接口返回码
 *
 * @author cj180
 * @date 2024年5月17日
 */
@Getter
public enum ResponseCode {
    HTTP_SUCCESS(10000, "请求成功！"),

    HTTP_FAILURE(90000, "请求失败！"),
    UNKNOWN_EXCEPTION(99999, "系统未知异常!"),

    AUTHORIZED_FAILURE(92001, "认证失败！"),
    LOGIN_FAILURE(92002, "登录失败！"),
    LOGOUT_FAILURE(92003, "推出登录失败！"),
    ACCESS_DENIED_FAILURE(92004, "访问失败！"),

    SYSTEM_EXCEPTION(93001, "后台系统异常!"),
    VALID_EXCEPTION(93002, "系统格式校验异常!"),
    PERMISSIONS_FAILURE(93003, "没有权限！"),

    CONTROLLER_PARAMS_FAILURE(91001, "请求参数有误！"),
    FREQUENT_OPERATE_FAILURE(92001, "操作频繁，请稍后再试！"),

    ES_INDEX_CREATE_EXCEPTION(96001, "创建索引失败!"),
    ES_INDEX_DELETE_EXCEPTION(96002, "删除索引失败!"),
    ES_INDEX_RE_CREATE_EXCEPTION(96003, "重新创建索引失败!"),
    ES_ADD_EXCEPTION(96004, "ES新增数据失败!"),
    ES_QUERY_EXCEPTION(96005, "ES查询数据失败!"),
    ES_DELETE_EXCEPTION(96006, "ES数据删除失败!"),

    PRODUCT_UP_EXCEPTION(96101, "产品上架异常!");

    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
