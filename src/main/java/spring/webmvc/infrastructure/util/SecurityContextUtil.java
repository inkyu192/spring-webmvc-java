package spring.webmvc.infrastructure.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityContextUtil {

	public static Long getMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new BadCredentialsException("인증 정보가 없습니다.");
		}

		if (!authentication.isAuthenticated()) {
			throw new BadCredentialsException("인증되지 않은 사용자입니다.");
		}

		try {
			return Long.parseLong(authentication.getPrincipal().toString());
		} catch (NumberFormatException e) {
			throw new BadCredentialsException("잘못된 인증 정보입니다.");
		}
	}

	public static Long getMemberIdOrNull() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		try {
			return Long.parseLong(authentication.getPrincipal().toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
