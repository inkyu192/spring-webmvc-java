package com.toy.shop.controller;

import com.toy.shop.dto.request.MemberSaveRequest;
import com.toy.shop.dto.request.MemberUpdateRequest;
import com.toy.shop.dto.response.MemberResponse;
import com.toy.shop.service.MemberService;
import com.toy.shop.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Object saveMember(@RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        MemberResponse responseDto = memberService.saveMember(memberSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public Object members(@RequestParam(required = false) String name, Pageable pageable) {
        Page<MemberResponse> members = memberService.members(name, pageable);

        return new ApiResponse<>(members);
    }

    @GetMapping("{id}")
    public Object member(@PathVariable Long id) {
        MemberResponse responseDto = memberService.member(id);

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping("{id}")
    public Object updateMember(@PathVariable Long id,
                               @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        MemberResponse responseDto = memberService.updateMember(id, memberUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("{id}")
    public Object deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return new ApiResponse<>();
    }
}
