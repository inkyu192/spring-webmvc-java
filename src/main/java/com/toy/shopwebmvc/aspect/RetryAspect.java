package com.toy.shopwebmvc.aspect;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] {} count={}/{}", joinPoint.getSignature(), retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception exception) {
                log.error("[retry] {}", joinPoint.getSignature(), exception);
            }
        }
        throw new CommonException(ApiResponseCode.INTERNAL_SERVER_ERROR);
    }
}
