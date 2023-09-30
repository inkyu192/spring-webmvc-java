package com.toy.shopwebmvc.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.toy.shopwebmvc.common.Constants.LOG_ID;


@Slf4j
@Component
public class SecondInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID.name());

        log.info("======================== SecondInterceptor preHandle [{}][{}][{}] ========================", logId, requestURI, handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID.name());

        log.info("======================== SecondInterceptor postHandle [{}][{}][{}] ========================", logId, requestURI, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID.name());

        log.info("======================== SecondInterceptor afterCompletion [{}][{}][{}] ========================", logId, requestURI, handler);
    }
}
