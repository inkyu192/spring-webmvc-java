package com.toy.shop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.toy.shop.common.Constants.LOG_ID;


@Slf4j
//@WebFilter(urlPatterns = "/*")
public class SecondFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("======================== SecondFilter init ========================");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String logId = (String) httpRequest.getAttribute(LOG_ID.name());

        try {
            log.info("======================== SecondFilter request [{}][{}] ========================", logId, requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("======================== SecondFilter error [{}] ========================", e.getMessage(), e);
            throw e;
        } finally {
            log.info("======================== SecondFilter response [{}][{}] ========================", logId, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("======================== SecondFilter destroy ========================");
    }
}
