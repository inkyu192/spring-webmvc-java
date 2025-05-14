package spring.webmvc.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.MenuService;
import spring.webmvc.presentation.dto.request.MenuCreateRequest;
import spring.webmvc.presentation.dto.response.MenuResponse;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MenuResponse createMenu(@RequestBody @Validated MenuCreateRequest menuCreateRequest) {
		return new MenuResponse(
			menuService.createMenu(
				menuCreateRequest.parentId(),
				menuCreateRequest.name(),
				menuCreateRequest.path(),
				menuCreateRequest.permissionIds()
			)
		);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<MenuResponse> findMenus() {
		return menuService.findMenus().stream()
			.map(MenuResponse::new)
			.toList();
	}
}
