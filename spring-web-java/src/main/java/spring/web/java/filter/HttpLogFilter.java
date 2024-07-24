package spring.web.java.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.dto.HttpLog;

@Slf4j
public class HttpLogFilter extends GenericFilterBean {

    public static final String TRANSACTION_ID = "transactionId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

        if (httpServletRequest.getRequestURI().startsWith("/actuator")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

        String transactionId = UUID.randomUUID().toString();
        servletRequest.setAttribute(TRANSACTION_ID, transactionId);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();

        HttpLog httpLog = new HttpLog(transactionId, requestWrapper, responseWrapper, (endTime - startTime) / 1000.0);
        httpLog.log();

        responseWrapper.copyBodyToResponse();
    }
}
