package spring.web.kotlin.presentation.controller

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.presentation.dto.request.MemberSaveRequest
import spring.web.kotlin.presentation.dto.request.MemberUpdateRequest
import spring.web.kotlin.application.service.MemberService
import spring.web.kotlin.presentation.dto.response.MemberResponse
import spring.web.kotlin.presentation.exception.AtLeastOneRequiredException

@RestController
@RequestMapping("members")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMember(@RequestBody @Validated memberSaveRequest: MemberSaveRequest): MemberResponse {
        if (memberSaveRequest.roleIds.isEmpty() && memberSaveRequest.permissionIds.isEmpty()) {
            throw AtLeastOneRequiredException("roleIds", "permissionIds")
        }

        return memberService.saveMember(memberSaveRequest)
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun findMember() = memberService.findMember()

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    fun updateMember(@RequestBody @Validated memberUpdateRequest: MemberUpdateRequest) =
        memberService.updateMember(memberUpdateRequest)

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember() {
        memberService.deleteMember()
    }
}
