package com.cayjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cayjin.backend.base.BasePage;
import com.cayjin.backend.base.BaseQuery;
import com.cayjin.backend.common.Constant;
import com.cayjin.backend.entity.User;
import com.cayjin.backend.entity.vo.EmailRegisterVO;
import com.cayjin.backend.entity.vo.UserDepVO;
import com.cayjin.backend.entity.vo.UserVO;
import com.cayjin.backend.mapper.UserMapper;
import com.cayjin.backend.service.UserService;
import com.cayjin.backend.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息表
 *
 * @author cj180
 * @date 2025-03-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowUtils flowUtils;

    @Resource
    PasswordEncoder passwordEncoder;


    @Override
    public BasePage queryPage(Map<String, Object> params) {
        IPage<UserDepVO> page = userMapper.getUsersWithDepName(new BaseQuery<UserDepVO>().getPage(params), params);
        return new BasePage(page);
    }

    @Override
    public UserVO getUserByNameOrEmail(String text) {
        User user = this.query().eq("username", text)
                .or().eq("email", text)
                .eq("is_deleted", 0)
                .one();

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;

    }

    @Override
    public String getPasswordByNameOrEmail(String text) {
        User user = this.query().eq("username", text)
                .or().eq("email", text)
                .eq("is_deleted", 0)
                .one();

        if (user != null) {
            return user.getPassword();
        } else {
            return null;
        }
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) return "请求频繁，请稍后在试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;// 生成6位随机数验证码
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue().set(Constant.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    @Override
    public String registerUserByEmail(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Constant.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);

        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "输入验证码错误，请重新输入";
        if (this.existUserByEmail(email)) return "此电子邮件已被其他用户注册";
        if (this.existUserByUsername(username)) return "此用户名已被其他用户注册";
        String password = passwordEncoder.encode(vo.getPassword());
        User user = new User(username, password, email, null, null, null, null, 1);
        if (this.save(user)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }


    @Override
    public String checkEmail(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String key = Constant.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);

        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "输入验证码错误";
        stringRedisTemplate.delete(key);
        stringRedisTemplate.opsForValue().set(Constant.RESET_EMAIL_FLAG + email, "reset", 3, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public String resetPassword(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String key = Constant.RESET_EMAIL_FLAG + email;
        String flag = stringRedisTemplate.opsForValue().get(key);

        if (!"reset".equals(flag)) return "请先获取验证码";
        String password = passwordEncoder.encode(vo.getPassword());
        User user = this.query().eq("email", email).one();
        user.setPassword(password);
        if (this.updateById(user)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }


    private boolean existUserByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return this.baseMapper.exists(wrapper);
    }

    private boolean existUserByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<User>query().eq("username", username));
    }

    private boolean verifyLimit(String ip) {
        String key = Constant.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

}
