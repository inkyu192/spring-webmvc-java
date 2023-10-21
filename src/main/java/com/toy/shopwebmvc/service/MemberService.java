package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.domain.Address;
import com.toy.shopwebmvc.domain.Member;
import com.toy.shopwebmvc.dto.request.MemberSaveRequest;
import com.toy.shopwebmvc.dto.request.MemberUpdateRequest;
import com.toy.shopwebmvc.dto.response.MemberResponse;
import com.toy.shopwebmvc.exception.CommonException;
import com.toy.shopwebmvc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.toy.shopwebmvc.constant.ApiResponseCode.DATA_DUPLICATE;
import static com.toy.shopwebmvc.constant.ApiResponseCode.DATA_NOT_FOUND;

@Slf4j
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

        Member member = Member.create()
                .account(memberSaveRequest.account())
                .password(passwordEncoder.encode(memberSaveRequest.password()))
                .name(memberSaveRequest.name())
                .address(Address.create()
                        .city(memberSaveRequest.city())
                        .street(memberSaveRequest.street())
                        .zipcode(memberSaveRequest.zipcode())
                        .build())
                .role(memberSaveRequest.role())
                .build();

        memberRepository.save(member);

        return new MemberResponse(member);
    }

    public Page<MemberResponse> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAll(pageable, account, name)
                .map(MemberResponse::new);
    }

    public MemberResponse findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::new)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND))
                .toBuilder()
                .name(memberUpdateRequest.name())
                .address(Address.create()
                        .city(memberUpdateRequest.city())
                        .street(memberUpdateRequest.street())
                        .zipcode(memberUpdateRequest.zipcode())
                        .build())
                .role(memberUpdateRequest.role())
                .build();

        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        memberRepository.delete(member);
    }
}
