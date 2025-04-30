package com.cayjin.backend.controller;

import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.common.ResponseCode;
import com.cayjin.backend.entity.vo.EmailRegisterVO;
import com.cayjin.backend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

/**
 * 认证控制器
 *
 * @author cj180
 * @date 2025年4月25日
 */
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    UserService userService;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request) {
        return this.messageHandle(() -> userService.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(() -> userService.registerUserByEmail(vo));
    }

    @PostMapping("/checkEmail")
    public RestBean<Void> checkEmail(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(() -> userService.checkEmail(vo));
    }

    @PostMapping("/resetPassword")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(() -> userService.resetPassword(vo));
    }

    /**
     * 处理消息
     *
     * @param action 消息处理函数
     * @return 处理结果
     */
    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(ResponseCode.HTTP_FAILURE.getCode(), message);
    }
}
