package com.toy.shop.controller;

import com.toy.shop.common.ResultDto;
import com.toy.shop.dto.MemberDto;
import com.toy.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Object saveMember(@RequestBody @Valid MemberDto.Save requestDto) {
        MemberDto.Response responseDto = memberService.saveMember(requestDto);

        return new ResultDto<>(responseDto);
    }

    @GetMapping
    public Object members(@RequestParam(required = false) String name, Pageable pageable) {
        Page<MemberDto.Response> members = memberService.members(name, pageable);

        return new ResultDto<>(members);
    }

    @GetMapping("/{id}")
    public Object member(@PathVariable Long id) {
        MemberDto.Response responseDto = memberService.member(id);

        return new ResultDto<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateMember(@PathVariable Long id,
                               @RequestBody @Valid MemberDto.Update requestDto) {
        MemberDto.Response responseDto = memberService.updateMember(id, requestDto);

        return new ResultDto<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return new ResultDto<>();
    }
}
