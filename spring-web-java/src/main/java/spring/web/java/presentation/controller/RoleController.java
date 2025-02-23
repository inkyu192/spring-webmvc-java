package spring.web.java.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.RoleService;
import spring.web.java.presentation.dto.request.RoleSaveRequest;
import spring.web.java.presentation.dto.response.RoleResponse;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RoleResponse saveRole(@RequestBody @Valid RoleSaveRequest roleSaveRequest) {
		return roleService.saveRole(roleSaveRequest);
	}
}
