package spring.web.kotlin.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.domain.member.dto.MemberSaveRequest
import spring.web.kotlin.domain.member.dto.MemberUpdateRequest
import spring.web.kotlin.domain.member.service.MemberService

@RestController
@RequestMapping("members")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMember(@RequestBody memberSaveRequest: MemberSaveRequest) = memberService.saveMember(memberSaveRequest)

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun findMember() = memberService.findMember()

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    fun updateMember(@RequestBody memberUpdateRequest: MemberUpdateRequest) =
        memberService.patchMember(memberUpdateRequest)

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember() {
        memberService.deleteMember()
    }
}
