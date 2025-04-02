package spring.webmvc.infrastructure.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityContextUtil {

	public static Long getMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new BadCredentialsException("인증되지 않은 사용자입니다.");
		}

		return Long.valueOf(String.valueOf(authentication.getPrincipal()));
	}
}
