package spring.webmvc.infrastructure.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextUtil {

	private SecurityContextUtil() {
	}

	public static Long getUserId() {
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

	public static Long getUserIdOrNull() {
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

	public static Set<String> getAuthorities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new BadCredentialsException("인증 정보가 없습니다.");
		}

		if (!authentication.isAuthenticated()) {
			throw new BadCredentialsException("인증되지 않은 사용자입니다.");
		}

		return authentication.getAuthorities().stream()
			.map(Object::toString)
			.collect(Collectors.toSet());
	}
}
