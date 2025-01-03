package spring.web.java.infrastructure.adapter.in;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.MemberServicePort;
import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.infrastructure.configuration.security.UserDetailsImpl;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberServicePort memberService;

	@PostMapping
	public MemberResponse saveMember(@RequestBody @Valid MemberSaveRequest memberSaveRequest) {
		return memberService.saveMember(memberSaveRequest);
	}

	@PostMapping("login")
	public TokenResponse login(@RequestBody LoginRequest loginRequest) {
		return memberService.login(loginRequest);
	}

	@GetMapping
	public MemberResponse findMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return memberService.findMember(userDetails.getMemberId());
	}

	@PatchMapping
	public MemberResponse updateMember(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody @Valid MemberUpdateRequest memberUpdateRequest
	) {
		return memberService.updateMember(userDetails.getMemberId(), memberUpdateRequest);
	}

	@DeleteMapping
	public void deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		memberService.deleteMember(userDetails.getMemberId());
	}
}
