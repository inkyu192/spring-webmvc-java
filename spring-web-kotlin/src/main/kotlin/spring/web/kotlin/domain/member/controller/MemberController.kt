package spring.web.kotlin.domain.member.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.domain.member.dto.MemberSaveRequest
import spring.web.kotlin.domain.member.dto.MemberUpdateRequest
import spring.web.kotlin.global.common.ApiResponse
import spring.web.kotlin.domain.member.service.MemberService

@RestController
@RequestMapping("member")
class MemberController(
    val memberService: MemberService
) {
    @PostMapping
    fun saveMember(@RequestBody memberSaveRequest: MemberSaveRequest): ApiResponse<Any> {
        return ApiResponse(memberService.saveMember(memberSaveRequest))
    }

    @GetMapping
    fun findMembers(
        pageable: Pageable,
        @RequestParam(required = false) account: String?,
        @RequestParam(required = false) name: String?
    ) = ApiResponse(memberService.findMembers(pageable, account, name))

    @GetMapping("{id}")
    fun findMember(@PathVariable id: Long) = ApiResponse(memberService.findMember(id))

    @PatchMapping("{id}")
    fun updateMember(
        @PathVariable id: Long,
        @RequestBody memberUpdateRequest: MemberUpdateRequest
    ) = ApiResponse(memberService.updateMember(id, memberUpdateRequest))

    @DeleteMapping("{id}")
    fun deleteMember(@PathVariable id: Long): ApiResponse<Unit> {
        memberService.deleteMember(id)

        return ApiResponse()
    }
}