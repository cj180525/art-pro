//package com.cayjin.backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Collections;
//
///**
// * 跨域配置
// *
// * @author cj180
// * @date 2025年3月18日
// */
//@Configuration
//public class CorsConfig {
//
//    // 此方法在没有spring security鉴权时可用
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        //1.允许任何来源
//        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        //2.允许任何请求头
//        corsConfiguration.addAllowedHeader("*");
//        //3.允许任何方法
//        corsConfiguration.addAllowedMethod("*");
//        //4.允许凭证
//        corsConfiguration.setAllowCredentials(true);
//        //5.预检请求的缓存时间（秒），即在这段时间里，对于相同的跨域请求不在预检
//        corsConfiguration.setMaxAge(18000L);
//        //6.配置前端js允许访问的自定义响应头，不能用*
//        corsConfiguration.addExposedHeader(HttpHeaders.ACCEPT);
//
//        // 导包时要导入org.springframework.web.cors包下的。不能用org.springframework.web.cors.reactive包下的。
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }
//
////    // 此方法在没有springcloud gateway网关项目中可用
////    @Bean
////    public FilterRegistrationBean corsFilterBean() {
////        CorsConfiguration corsConfiguration = new CorsConfiguration();
////        //1.允许任何来源
//////        corsConfiguration.addAllowedOrigin("*");
////        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
////        //2.允许任何请求头
////        corsConfiguration.addAllowedHeader("*");
////        //3.允许任何方法
////        corsConfiguration.addAllowedMethod("*");
////        //4.允许凭证
////        corsConfiguration.setAllowCredentials(true);
////
////        // 导包时要导入org.springframework.web.cors包下的。不能用org.springframework.web.cors.reactive包下的。
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", corsConfiguration);
////
////        // 小于SpringSecurity Filter的 Order(-100) 即可
////        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
////        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
////        return bean;
////    }
//
//}