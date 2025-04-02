package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.RoleService;
import spring.webmvc.presentation.dto.request.RoleSaveRequest;
import spring.webmvc.presentation.dto.response.RoleResponse;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RoleResponse saveRole(@RequestBody @Validated RoleSaveRequest roleSaveRequest) {
		return roleService.saveRole(roleSaveRequest);
	}
}
