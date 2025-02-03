package spring.web.java.global.common;

import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

	public static Long getMemberId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Long.valueOf(String.valueOf(principal));
	}
}
