package spring.webmvc.application.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.MenuResult;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;

	@Transactional
	public MenuResult createMenu(Long parentId, String name, String path) {
		Menu menu;

		if (parentId == null) {
			menu = Menu.create(name, path);
		} else {
			Menu parent = menuRepository.findById(parentId)
				.orElseThrow(() -> new EntityNotFoundException(Menu.class, parentId));

			menu = Menu.create(name, path, parent);
		}

		menuRepository.save(menu);

		return mapToMenuResult(menu);
	}

	public List<MenuResult> findMenus() {
		Set<String> permissions = SecurityContextUtil.getAuthorities();

		return menuRepository.findRootMenus(permissions).stream()
			.map(this::mapToMenuResult)
			.toList();
	}

	private MenuResult mapToMenuResult(Menu menu) {
		Set<String> permissions = SecurityContextUtil.getAuthorities();
		List<Menu> childMenus = menuRepository.findChildMenus(permissions, menu.getId());

		return new MenuResult(
			menu.getId(),
			menu.getName(),
			menu.getPath(),
			childMenus.stream()
				.map(this::mapToMenuResult)
				.toList()
		);
	}
}
