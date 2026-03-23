package spring.webmvc.application.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.TranslationRepository;

@Service
@RequiredArgsConstructor
public class TranslationService {

	private final TranslationRepository translationRepository;

	private Cache<String, String> cache = Caffeine.newBuilder().build();

	@Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
	public void reloadCache() {
		Cache<String, String> newCache = Caffeine.newBuilder().build();

		translationRepository.findAll().forEach(translation ->
			newCache.put(
				translation.getCode() + ":" + translation.getLocale(),
				translation.getMessage()
			)
		);

		cache = newCache;
	}

	private String resolveMessage(String code, Locale locale, Object... args) {
		String message = cache.getIfPresent(code + ":" + locale.getLanguage());
		if (message == null) {
			message = cache.getIfPresent(code + ":" + Locale.ENGLISH.getLanguage());
		}
		if (message == null) {
			return null;
		}
		return new MessageFormat(message, locale).format(args);
	}

	public String getMessage(String code, Locale locale, Object... args) {
		String message = resolveMessage(code, locale, args);
		if (message == null) {
			throw new IllegalStateException();
		}
		return message;
	}

	public String getMessageOrNull(String code, Locale locale, Object... args) {
		return resolveMessage(code, locale, args);
	}
}
