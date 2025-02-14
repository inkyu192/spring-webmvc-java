package spring.web.java.infrastructure.util;

import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityContextUtil {

	public static Long getMemberId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Long.valueOf(String.valueOf(principal));
	}
}
