package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.MemberSaveRequest;
import com.toy.shopwebmvc.dto.request.MemberUpdateRequest;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.MemberResponse;
import com.toy.shopwebmvc.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ApiResponse<MemberResponse> saveMember(@RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        MemberResponse responseDto = memberService.saveMember(memberSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public ApiResponse<Page<MemberResponse>> findMembers(
            Pageable pageable,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String name
    ) {
        Page<MemberResponse> members = memberService.findMembers(pageable, account, name);

        return new ApiResponse<>(members);
    }

    @GetMapping("{id}")
    public ApiResponse<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse responseDto = memberService.findMember(id);

        return new ApiResponse<>(responseDto);
    }

    @PutMapping("{id}")
    public ApiResponse<MemberResponse> updateMember(
            @PathVariable Long id,
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest
    ) {
        MemberResponse responseDto = memberService.updateMember(id, memberUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return new ApiResponse<>();
    }
}
