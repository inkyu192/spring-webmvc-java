package spring.web.java.global.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

	public Long getMemberId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Long.valueOf(String.valueOf(principal));
	}
}
