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

        return MemberResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .name(member.getName())
                .city(member.getAddress().getCity())
                .street(member.getAddress().getStreet())
                .zipcode(member.getAddress().getCity())
                .role(member.getRole())
                .build();
    }

    public Page<MemberResponse> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAll(pageable, account, name)
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getAddress().getCity())
                        .street(member.getAddress().getStreet())
                        .zipcode(member.getAddress().getCity())
                        .role(member.getRole())
                        .build());
    }

    public MemberResponse findMember(Long id) {
        return memberRepository.findById(id)
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getAddress().getCity())
                        .street(member.getAddress().getStreet())
                        .zipcode(member.getAddress().getCity())
                        .role(member.getRole())
                        .build())
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));

        member.update(
                memberUpdateRequest.name(),
                Address.create()
                        .city(memberUpdateRequest.city())
                        .street(memberUpdateRequest.street())
                        .zipcode(memberUpdateRequest.zipcode())
                        .build(),
                memberUpdateRequest.role()
        );

        return MemberResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .name(member.getName())
                .city(member.getAddress().getCity())
                .street(member.getAddress().getStreet())
                .zipcode(member.getAddress().getCity())
                .role(member.getRole())
                .build();
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
