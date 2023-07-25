package com.toy.shop.business.member.service;

import com.toy.shop.business.member.dto.request.MemberLoginRequest;
import com.toy.shop.config.JwtTokenProvider;
import com.toy.shop.business.member.dto.response.MemberLoginResponse;
import com.toy.shop.domain.Member;
import com.toy.shop.business.member.dto.request.MemberSaveRequest;
import com.toy.shop.business.member.dto.request.MemberUpdateRequest;
import com.toy.shop.business.member.dto.response.MemberResponse;
import com.toy.shop.exception.CommonException;
import com.toy.shop.business.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.toy.shop.common.ApiResponseCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(memberLoginRequest.account(), memberLoginRequest.password());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return new MemberLoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
        Member member = Member.createMember(memberSaveRequest, passwordEncoder);

        memberRepository.save(member);

        return new MemberResponse(member);
    }

    @Override
    public Page<MemberResponse> members(String name, Pageable pageable) {
        return memberRepository.findAllOfQueryMethod(name, pageable)
                .map(MemberResponse::new);
    }

    @Override
    public MemberResponse member(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::new)
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        member.updateMember(requestDto, passwordEncoder);

        return new MemberResponse(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }
}
