package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.domain.Address;
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


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

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

        return MemberResponse.create(member);
    }

    public Page<MemberResponse> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAllWithQuerydsl(pageable, account, name)
                .map(MemberResponse::create);
    }

    public MemberResponse findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::create)
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

        return MemberResponse.create(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
