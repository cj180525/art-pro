package com.cayjin.backend.filter;

import com.cayjin.backend.base.RestBean;
import com.cayjin.backend.common.Constant;
import com.cayjin.backend.common.ResponseCode;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author cj180
 * @description: TODO
 * @date 2023/8/15 22:12
 */
@Component
@Order(Constant.LIMIT_ORDER)
public class FlowLimitFilter extends HttpFilter {

    @Resource
    StringRedisTemplate template;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        String addr = request.getRemoteAddr();
//        if (this.tryCount(addr)) {
            chain.doFilter(request, response);
//        } else {
//            this.writeBlockMessage(response);
//        }
    }


    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.failure(ResponseCode.FREQUENT_OPERATE_FAILURE).asJsonString());


    }

    private boolean tryCount(String ip) {
        synchronized (ip.intern()) {
            if (Boolean.TRUE.equals(template.hasKey(Constant.FLOW_LIMIT_BLOCK + ip))) {
                return false;
            }
            return this.limitPeriodCheck(ip);
        }
    }

    private boolean limitPeriodCheck(String ip) {
        if (Boolean.TRUE.equals(template.hasKey(Constant.FLOW_LIMIT_COUNTER + ip))) {
            long increment = Optional.ofNullable(template.opsForValue().
                    increment(Constant.FLOW_LIMIT_COUNTER + ip)).orElse(0L);
            if (increment > 10) {
                template.opsForValue().set(Constant.FLOW_LIMIT_BLOCK + ip, "1", 3, TimeUnit.SECONDS);
                return false;
            }
            return true;
        } else {
            template.opsForValue().set(Constant.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS);
            return true;
        }
    }
}
