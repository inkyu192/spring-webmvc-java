package com.toy.shop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static com.toy.shop.common.Constants.*;

@Slf4j
//@Component
public class FirstFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("======================== FirstFilter init ========================");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String logId = UUID.randomUUID().toString();
        httpRequest.setAttribute(LOG_ID, logId);

        try {
            log.info("======================== FirstFilter request [{}][{}] ========================", logId, requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("======================== FirstFilter error [{}] ========================", e.getMessage(), e);
            throw e;
        } finally {
            log.info("======================== FirstFilter response [{}][{}] ========================", logId, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("======================== FirstFilter destroy ========================");
    }
}
