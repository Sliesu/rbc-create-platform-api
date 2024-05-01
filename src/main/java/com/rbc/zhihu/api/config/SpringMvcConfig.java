package com.rbc.zhihu.api.config;

import com.rbc.zhihu.api.interceptor.ImageProcessingInterceptor;
import com.rbc.zhihu.api.interceptor.JwtAuthenticationInterceptor;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author DingYihang
 */
@Configuration
@AllArgsConstructor
@ComponentScan("com.rbc.zhihu.api.interceptor")
public class SpringMvcConfig implements WebMvcConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    private final ImageProcessingInterceptor imageProcessingInterceptor;

    // CORS配置
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    // 拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/swagger-ui.html", "/doc.html", "/webjars/**",
                        "/swagger-resources/**", "/v2/api-docs", "/v2/api-docs/**",
                        "/swagger-ui/**", "/configuration/ui", "/configuration/security",
                        "/api/v1/user/login/**", "/api/v1/sms/**", "/api/v1/special/**");
        registry.addInterceptor(imageProcessingInterceptor)
                .addPathPatterns("/images/**");
    }

    // 静态资源映射，包括favicon.ico
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-resources/**").addResourceLocations("classpath:/META-INF/resources/swagger-resources/");
        registry.addResourceHandler("/v2/api-docs").addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");
    }
}