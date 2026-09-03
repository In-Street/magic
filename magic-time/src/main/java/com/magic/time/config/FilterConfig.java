package com.magic.time.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Cheng Yufei
 * @create 2026-07-01 17:05
 **/
@Configuration
public class FilterConfig {
    
    // @Bean
    public FilterRegistrationBean requestFilter(){

        OncePerRequestFilter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                filterChain.doFilter(request,response);
            }
        };

        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(filter);
        filterFilterRegistrationBean.addUrlPatterns(new String[]{
                ""
        });
        return filterFilterRegistrationBean;
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:7070");
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.setAllowedMethods(Lists.newArrayList("GET","POST","PUT","OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(source);
        return corsFilter;
    }
}
