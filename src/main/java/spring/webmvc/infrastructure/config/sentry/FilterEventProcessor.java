package spring.webmvc.infrastructure.config.sentry;

import java.util.Set;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.sentry.EventProcessor;
import io.sentry.Hint;
import io.sentry.SentryEvent;

@Component
public class FilterEventProcessor implements EventProcessor {

	private static final Set<Class<? extends Exception>> IGNORED_EXCEPTIONS = Set.of(
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		MethodArgumentConversionNotSupportedException.class,
		NoResourceFoundException.class,
		HttpRequestMethodNotSupportedException.class,
		ServletRequestBindingException.class
	);

	@Override
	public SentryEvent process(SentryEvent event, Hint hint) {
		if (shouldFilter(event.getThrowable())) {
			return null;
		}

		return event;
	}

	private boolean shouldFilter(Throwable throwable) {
		return throwable != null && IGNORED_EXCEPTIONS.contains(throwable.getClass());
	}
}
