package spring.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.web.java.config.security.JwtTokenProvider;
import spring.web.java.config.security.UserDetailsImpl;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.domain.Address;
import spring.web.java.domain.Member;
import spring.web.java.domain.Token;
import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.exception.CommonException;
import spring.web.java.repository.MemberRepository;
import spring.web.java.repository.TokenRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
        memberRepository.findByAccount(memberSaveRequest.account())
                .ifPresent(member -> {
                    throw new CommonException(ApiResponseCode.DATA_DUPLICATE);
                });

        Member member = Member.create(
                memberSaveRequest.account(),
                passwordEncoder.encode(memberSaveRequest.password()),
                memberSaveRequest.name(),
                memberSaveRequest.role(),
                Address.create(
                        memberSaveRequest.city(),
                        memberSaveRequest.street(),
                        memberSaveRequest.zipcode()
                )
        );

        memberRepository.save(member);

        return new MemberResponse(member);
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new CommonException(ApiResponseCode.BAD_CREDENTIALS);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.createAccessToken(userDetails.getMemberId(), userDetails.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        tokenRepository.save(Token.create(userDetails.getMemberId(), refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }

    public MemberResponse findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::new)
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));

        member.update(
                memberUpdateRequest.name(),
                memberUpdateRequest.role(),
                Address.create(
                        memberUpdateRequest.city(),
                        memberUpdateRequest.street(),
                        memberUpdateRequest.zipcode()
                )
        );

        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
