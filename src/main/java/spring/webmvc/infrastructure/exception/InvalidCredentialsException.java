package spring.webmvc.infrastructure.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {

	public InvalidCredentialsException() {
		super(null);
	}
}
