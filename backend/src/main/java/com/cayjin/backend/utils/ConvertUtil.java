package com.cayjin.backend.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * DO转化为VO
 *
 * @author cj180
 * @date 2024年4月12日
 */
public class ConvertUtil {

    public static <V> V convertVO(Object source, Class<V> clazz) {
        try {
            V v = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, v);
            return v;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <V,T> List<V> convertVOList(List<T> sources, Class<V> clazz) {
        return sources.stream().map(source -> convertVO(source, clazz)).toList();
    }

}
