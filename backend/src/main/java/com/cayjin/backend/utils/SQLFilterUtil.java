package com.cayjin.backend.utils;

import com.cayjin.backend.base.BaseException;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL过滤
 *
 * @author cj180
 * @date 2024年4月12日
 */
public class SQLFilterUtil {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.contains(keyword)){
                throw new BaseException("包含非法字符");
            }
        }

        return str;
    }
}
