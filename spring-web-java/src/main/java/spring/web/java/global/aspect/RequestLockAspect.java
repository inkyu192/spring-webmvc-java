package spring.web.java.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.requestlock.service.RequestLockService;
import spring.web.java.global.common.SecurityService;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestLockAspect {

	private final HttpServletRequest httpServletRequest;
	private final RequestLockService requestLockService;
	private final SecurityService securityService;

	@Pointcut("@annotation(spring.web.java.global.aspect.RequestLock)")
	public void preventDuplicateRequestAnnotation() {}

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void withinRestController() {}

	@Pointcut("within(@org.springframework.security.access.prepost.PreAuthorize *)")
	public void withinPreAuthorize() {}

	@Around("preventDuplicateRequestAnnotation() && withinRestController() && withinPreAuthorize()")
	public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
		String uri = httpServletRequest.getRequestURI();
		String method = httpServletRequest.getMethod();
		Long memberId = securityService.getMemberId();

		requestLockService.validate(memberId, method, uri);

		return joinPoint.proceed();
	}
}
