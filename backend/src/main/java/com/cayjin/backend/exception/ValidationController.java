package com.cayjin.backend.exception;

import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.common.ResponseCode;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 处理Controller请求参数校验异常
 *
 * @author cj180
 * @date 2025年4月25日
 */
@RestControllerAdvice
@Slf4j
public class ValidationController {

    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validationException(ValidationException exception) {
        log.warn("Resolve [{}：{}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(ResponseCode.CONTROLLER_PARAMS_FAILURE);
    }
}
