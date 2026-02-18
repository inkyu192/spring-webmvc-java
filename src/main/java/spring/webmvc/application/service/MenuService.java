package spring.webmvc.application.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

	public List<MenuResult> findMenus() {
		Set<String> permissions = SecurityContextUtil.getAuthorities();

		if (permissions.isEmpty()) {
			return List.of();
		}

		List<Menu> allMenus = getParentMenus(menuRepository.findAllByPermissions(permissions));

		List<Menu> rootMenus = allMenus.stream()
			.filter(menu -> menu.getParent() == null)
			.toList();

		return rootMenus.stream()
			.map(menu -> mapToResult(menu, allMenus))
			.toList();
	}

	public List<Menu> getParentMenus(List<Menu> menus) {
		List<Long> parentIds = menus.stream()
			.map(Menu::getParent)
			.filter(Objects::nonNull)
			.map(Menu::getId)
			.filter(Objects::nonNull)
			.distinct()
			.toList();

		if (parentIds.isEmpty()) {
			return menus;
		}

		List<Menu> parentMenus = menuRepository.findAllById(parentIds);

		Set<Menu> allMenus = new LinkedHashSet<>();
		allMenus.addAll(menus);
		allMenus.addAll(getParentMenus(parentMenus));

		return new ArrayList<>(allMenus);
	}

	private MenuResult mapToResult(Menu menu, List<Menu> allMenus) {
		List<MenuResult> children = allMenus.stream()
			.filter(m -> m.getParent() != null)
			.filter(m -> Objects.equals(m.getParent().getId(), menu.getId()))
			.sorted(Comparator.comparing(Menu::getSortOrder))
			.map(child -> mapToResult(child, allMenus))
			.toList();

		return new MenuResult(
			menu.getId(),
			menu.getName(),
			menu.getPath(),
			children
		);
	}
}
