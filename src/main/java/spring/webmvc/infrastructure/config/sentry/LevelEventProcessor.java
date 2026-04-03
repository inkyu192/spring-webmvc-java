package spring.webmvc.infrastructure.config.sentry;

import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.sentry.EventProcessor;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import spring.webmvc.infrastructure.exception.DuplicateEntityException;
import spring.webmvc.infrastructure.exception.DuplicateRequestException;
import spring.webmvc.infrastructure.exception.InsufficientQuantityException;
import spring.webmvc.infrastructure.exception.InvalidCredentialsException;
import spring.webmvc.infrastructure.exception.InvalidEntityStatusException;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Component
public class LevelEventProcessor implements EventProcessor {

	private static final Set<Class<? extends Exception>> WARNING_EXCEPTIONS = Set.of(
		NotFoundEntityException.class,
		DuplicateEntityException.class,
		DuplicateRequestException.class,
		InvalidEntityStatusException.class,
		InsufficientQuantityException.class,
		InvalidCredentialsException.class,
		AccessDeniedException.class,
		JwtException.class
	);

	@Override
	public SentryEvent process(SentryEvent event, Hint hint) {
		if (isWarning(event.getThrowable())) {
			event.setLevel(SentryLevel.WARNING);
		} else {
			event.setLevel(SentryLevel.ERROR);
		}

		return event;
	}

	private boolean isWarning(Throwable throwable) {
		return throwable != null && WARNING_EXCEPTIONS.stream()
			.anyMatch(e -> e.isAssignableFrom(throwable.getClass()));
	}
}
