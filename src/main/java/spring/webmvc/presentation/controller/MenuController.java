package spring.webmvc.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.MenuResult;
import spring.webmvc.application.service.MenuService;
import spring.webmvc.presentation.dto.response.MenuResponse;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<MenuResponse> findMenus() {
		List<MenuResult> menus = menuService.findMenus();

		return menus.stream().map(MenuResponse::of).toList();
	}
}

