package spring.web.kotlin.presentation.controller

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.application.service.RoleService
import spring.web.kotlin.presentation.dto.request.RoleSaveRequest

@RestController
@RequestMapping("/roles")
class RoleController(
    private val roleService: RoleService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveRole(@RequestBody @Validated roleSaveRequest: RoleSaveRequest) = roleService.saveRole(roleSaveRequest)
}