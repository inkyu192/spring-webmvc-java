package spring.web.java.infrastructure.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import spring.web.java.application.port.in.MemberServicePort;
import spring.web.java.infrastructure.configuration.security.UserDetailsImpl;
import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.ApiResponse;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.dto.response.TokenResponse;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServicePort memberService;

    @PostMapping
    public ApiResponse<MemberResponse> saveMember(@RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        MemberResponse responseDto = memberService.saveMember(memberSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @PostMapping("login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = memberService.login(loginRequest);

        return new ApiResponse<>(tokenResponse);
    }

    @GetMapping
    public ApiResponse<MemberResponse> findMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MemberResponse responseDto = memberService.findMember(userDetails.getMemberId());

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping
    public ApiResponse<MemberResponse> updateMember(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest
    ) {
        MemberResponse responseDto = memberService.updateMember(userDetails.getMemberId(), memberUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.deleteMember(userDetails.getMemberId());

        return new ApiResponse<>();
    }
}
