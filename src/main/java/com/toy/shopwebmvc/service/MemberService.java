package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.dto.request.MemberSaveRequest;
import com.toy.shopwebmvc.dto.request.MemberUpdateRequest;
import com.toy.shopwebmvc.dto.response.MemberResponse;
import com.toy.shopwebmvc.repository.MemberRepository;
import com.toy.shopwebmvc.repository.RoleRepository;
import com.toy.shopwebmvc.common.ApiResponseCode;
import com.toy.shopwebmvc.domain.Member;
import com.toy.shopwebmvc.domain.Role;
import com.toy.shopwebmvc.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.toy.shopwebmvc.common.ApiResponseCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
        Role role = roleRepository.findById(memberSaveRequest.roleId())
                .orElseThrow(() -> new CommonException(ApiResponseCode.ROLE_NOT_FOUND));

        Member member = Member.createMember(memberSaveRequest, role, passwordEncoder);

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
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        member.updateMember(requestDto, passwordEncoder);

        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }
}
