package spring.webmvc.infrastructure.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.RequestLockService;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestLockAspect {

	private final HttpServletRequest httpServletRequest;
	private final RequestLockService requestLockService;

	@Pointcut("@annotation(spring.webmvc.infrastructure.aspect.RequestLock)")
	public void preventDuplicateRequestAnnotation() {
	}

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void withinRestController() {
	}

	@Pointcut("within(@org.springframework.security.access.prepost.PreAuthorize *)")
	public void withinPreAuthorize() {
	}

	@Around("preventDuplicateRequestAnnotation() && withinRestController() && withinPreAuthorize()")
	public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
		String uri = httpServletRequest.getRequestURI();
		String method = httpServletRequest.getMethod();
		Long memberId = SecurityContextUtil.getMemberId();

		requestLockService.validate(memberId, method, uri);

		return joinPoint.proceed();
	}
}
