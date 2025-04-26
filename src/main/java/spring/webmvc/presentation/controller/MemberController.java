package spring.webmvc.presentation.controller;

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
import spring.webmvc.application.service.MemberService;
import spring.webmvc.presentation.dto.request.MemberCreateRequest;
import spring.webmvc.presentation.dto.request.MemberUpdateRequest;
import spring.webmvc.presentation.dto.response.MemberResponse;
import spring.webmvc.presentation.exception.AtLeastOneRequiredException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MemberResponse createMember(@RequestBody @Validated MemberCreateRequest memberCreateRequest) {
		List<Long> roleIds = memberCreateRequest.roleIds();
		List<Long> permissionIds = memberCreateRequest.permissionIds();

		if (ObjectUtils.isEmpty(roleIds) && ObjectUtils.isEmpty(permissionIds)) {
			throw new AtLeastOneRequiredException("roleIds", "permissionIds");
		}

		return new MemberResponse(
			memberService.createMember(
				memberCreateRequest.account(),
				memberCreateRequest.password(),
				memberCreateRequest.name(),
				memberCreateRequest.phone(),
				memberCreateRequest.birthDate(),
				memberCreateRequest.roleIds(),
				memberCreateRequest.permissionIds()
			)
		);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public MemberResponse findMember() {
		return new MemberResponse(memberService.findMember());
	}

	@PatchMapping
	@PreAuthorize("isAuthenticated()")
	public MemberResponse updateMember(@RequestBody @Validated MemberUpdateRequest memberUpdateRequest) {
		return new MemberResponse(
			memberService.updateMember(
				memberUpdateRequest.password(),
				memberUpdateRequest.name(),
				memberUpdateRequest.phone(),
				memberUpdateRequest.birthDate()
			)
		);
	}

	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMember() {
		memberService.deleteMember();
	}
}
