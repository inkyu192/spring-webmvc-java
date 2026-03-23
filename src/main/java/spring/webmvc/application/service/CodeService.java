package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import spring.webmvc.application.dto.result.CodeGroupResult;
import spring.webmvc.application.dto.result.CodeResult;

@Service
public class CodeService {

	private final TranslationService translationService;
	private final Map<String, List<Enum<?>>> codeRegistry;

	public CodeService(TranslationService translationService) {
		this.translationService = translationService;

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(Enum.class));

		this.codeRegistry = provider.findCandidateComponents("spring.webmvc.domain.model.enums")
			.stream()
			.map(beanDef -> {
				try {
					Class<?> clazz = Class.forName(beanDef.getBeanClassName());
					List<Enum<?>> constants = List.of((Enum<?>[])clazz.getEnumConstants());
					return Map.entry(clazz.getSimpleName(), constants);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			})
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public List<CodeGroupResult> findCodes() {
		var locale = LocaleContextHolder.getLocale();

		return codeRegistry.entrySet().stream()
			.map(entry -> new CodeGroupResult(
				entry.getKey(),
				translationService.getMessage(entry.getKey(), locale),
				entry.getValue().stream()
					.map(code -> new CodeResult(
						code.name(),
						translationService.getMessage(entry.getKey() + "." + code.name(), locale)
					))
					.toList()
			))
			.toList();
	}
}
