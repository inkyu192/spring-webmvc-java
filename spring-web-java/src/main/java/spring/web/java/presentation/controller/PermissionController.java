package spring.web.java.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.PermissionService;
import spring.web.java.presentation.dto.response.PermissionResponse;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping
	public List<PermissionResponse> findPermissions() {
		return permissionService.findAll();
	}
}
