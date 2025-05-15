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
	private final PermissionService permissionService;

	@Transactional
	public MenuResult createMenu(Long parentId, String name, String path, List<Long> permissionIds) {
		Menu menu = Menu.create(name, path);

		Menu parent = null;
		if (parentId != null) {
			parent = menuRepository.findById(parentId)
				.orElseThrow(() -> new EntityNotFoundException(Menu.class, parentId));
		}

		if (parent != null) {
			menu.updateParent(parent);
		}

		permissionService.addPermission(permissionIds, menu::addPermission);

		return new MenuResult(menuRepository.save(menu));
	}

	public List<MenuResult> findMenus() {
		Set<String> permissions = SecurityContextUtil.getAuthorities();

		return menuRepository.findAllByPermissionNameIn(permissions).stream()
			.filter(menu -> menu.getParent() == null)
			.map(MenuResult::new)
			.toList();
	}
}
