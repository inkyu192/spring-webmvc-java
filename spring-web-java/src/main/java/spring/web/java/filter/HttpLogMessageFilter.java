package spring.web.java.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import spring.web.java.dto.HttpLogMessage;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class HttpLogMessageFilter extends GenericFilterBean {

    public static final String TRANSACTION_ID = "transactionId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String transactionId = UUID.randomUUID().toString();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        servletRequest.setAttribute(TRANSACTION_ID, transactionId);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();

        HttpLogMessage httpLogMessage = new HttpLogMessage(
                transactionId,
                requestWrapper,
                responseWrapper,
                (endTime - startTime) / 1000.0
        );
        log.info(httpLogMessage.getMessage());

        responseWrapper.copyBodyToResponse();
    }
}
