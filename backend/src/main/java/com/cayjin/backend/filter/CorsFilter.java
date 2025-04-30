package com.cayjin.backend.filter;//package com.cayjin.backend.filter;
//
//import com.cayjin.backend.common.Const;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * @author cj180
// * @description: TODO
// * @date 2023/8/13 17:08
// */
//@Component
////@Order(Const.CORS_ORDER)
//public class CorsFilter extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        this.addCorsHeader(request, response);
//        chain.doFilter(request, response);
//    }
//
//    private void addCorsHeader(HttpServletRequest request, HttpServletResponse response) {
//        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
//    }
//}
