package spring.web.java.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.MemberService;
import spring.web.java.presentation.dto.request.MemberSaveRequest;
import spring.web.java.presentation.dto.request.MemberUpdateRequest;
import spring.web.java.presentation.dto.response.MemberResponse;
import spring.web.java.presentation.exception.AtLeastOneRequiredException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MemberResponse saveMember(@RequestBody @Validated MemberSaveRequest memberSaveRequest) {
		List<Long> roleIds = memberSaveRequest.roleIds();
		List<Long> permissionIds = memberSaveRequest.permissionIds();

		if (ObjectUtils.isEmpty(roleIds) && ObjectUtils.isEmpty(permissionIds)) {
			throw new AtLeastOneRequiredException("roleIds", "permissionIds");
		}

		return memberService.saveMember(memberSaveRequest);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public MemberResponse findMember() {
		return memberService.findMember();
	}

	@PatchMapping
	@PreAuthorize("isAuthenticated()")
	public MemberResponse updateMember(@RequestBody @Validated MemberUpdateRequest memberUpdateRequest) {
		return memberService.updateMember(memberUpdateRequest);
	}

	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMember() {
		memberService.deleteMember();
	}
}
