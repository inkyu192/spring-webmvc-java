package spring.web.java.domain.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.member.dto.MemberLoginRequest;
import spring.web.java.domain.member.dto.MemberResponse;
import spring.web.java.domain.member.dto.MemberSaveRequest;
import spring.web.java.domain.member.dto.MemberUpdateRequest;
import spring.web.java.domain.member.serivce.MemberService;
import spring.web.java.domain.token.dto.TokenResponse;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public MemberResponse saveMember(@RequestBody @Valid MemberSaveRequest memberSaveRequest) {
		return memberService.saveMember(memberSaveRequest);
	}

	@PostMapping("login")
	public TokenResponse login(@RequestBody MemberLoginRequest memberLoginRequest) {
		return memberService.login(memberLoginRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public MemberResponse findMember() {
		return memberService.findMember();
	}

	@PreAuthorize("isAuthenticated()")
	@PatchMapping
	public MemberResponse updateMember(@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
		return memberService.updateMember(memberUpdateRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping
	public void deleteMember() {
		memberService.deleteMember();
	}
}
