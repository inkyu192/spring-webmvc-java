package spring.webmvc.application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.cache.RequestLockCacheRepository;
import spring.webmvc.infrastructure.exception.DuplicateRequestException;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestLockAspect {

	private final HttpServletRequest httpServletRequest;
	private final RequestLockCacheRepository requestLockCacheRepository;
	private final ObjectMapper objectMapper;

	@Pointcut("@annotation(spring.webmvc.application.aspect.RequestLock)")
	public void preventDuplicateRequestAnnotation() {
	}

	@Around("preventDuplicateRequestAnnotation()")
	public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
		String uri = httpServletRequest.getRequestURI();
		String method = httpServletRequest.getMethod();

		String hash;

		try {
			hash = objectMapper.writeValueAsString(joinPoint.getArgs());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		boolean isSuccess = requestLockCacheRepository.tryLock(method, uri, hash);

		if (!isSuccess) {
			throw new DuplicateRequestException(method, uri);
		}

		return joinPoint.proceed();
	}
}
