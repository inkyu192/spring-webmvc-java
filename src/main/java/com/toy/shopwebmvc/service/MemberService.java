package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.domain.Member;
import com.toy.shopwebmvc.dto.request.MemberSaveRequest;
import com.toy.shopwebmvc.dto.request.MemberUpdateRequest;
import com.toy.shopwebmvc.dto.response.MemberResponse;
import com.toy.shopwebmvc.exception.CommonException;
import com.toy.shopwebmvc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

import static com.toy.shopwebmvc.constant.ApiResponseCode.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
        Optional<Member> account = memberRepository.findByAccount(memberSaveRequest.account());

        if (account.isPresent()) throw new CommonException(DATA_DUPLICATE);

        Member member = Member.createMember(memberSaveRequest, passwordEncoder);

        memberRepository.save(member);

        return new MemberResponse(member);
    }

    public Page<MemberResponse> members(String name, Pageable pageable) {
        return memberRepository.findAllOfQueryMethod(name, pageable)
                .map(MemberResponse::new);
    }

    public MemberResponse member(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::new)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        member.updateMember(requestDto, passwordEncoder);

        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        memberRepository.delete(member);
    }
}
