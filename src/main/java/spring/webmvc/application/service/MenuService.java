package spring.webmvc.application.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.MenuResult;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final TranslationService translationService;

	public List<MenuResult> findMenus() {
		Set<String> permissions = SecurityContextUtil.getAuthorities();

		if (permissions.isEmpty()) {
			return List.of();
		}

		List<Menu> allMenus = menuRepository.findAllWithRecursiveByPermissions(permissions);
		Locale locale = LocaleContextHolder.getLocale();

		List<Menu> rootMenus = allMenus.stream()
			.filter(menu -> menu.getParent() == null)
			.toList();

		return rootMenus.stream()
			.map(menu -> mapToResult(menu, allMenus, locale))
			.toList();
	}

	private MenuResult mapToResult(Menu menu, List<Menu> allMenus, Locale locale) {
		List<MenuResult> children = allMenus.stream()
			.filter(m -> m.getParent() != null)
			.filter(m -> Objects.equals(m.getParent().getId(), menu.getId()))
			.sorted(Comparator.comparing(Menu::getSortOrder))
			.map(child -> mapToResult(child, allMenus, locale))
			.toList();

		return new MenuResult(
			menu.getId(),
			translationService.getMessage(menu.getTranslationCode(), locale),
			menu.getPath(),
			children
		);
	}
}