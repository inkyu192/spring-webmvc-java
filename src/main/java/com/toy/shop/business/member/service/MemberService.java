package com.toy.shop.business.member.service;

import com.toy.shop.business.member.dto.request.MemberLoginRequest;
import com.toy.shop.business.member.dto.request.MemberSaveRequest;
import com.toy.shop.business.member.dto.request.MemberUpdateRequest;
import com.toy.shop.business.member.dto.response.MemberResponse;
import com.toy.shop.business.member.dto.response.MemberLoginResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    MemberLoginResponse login(MemberLoginRequest requestDto);

    MemberResponse saveMember(MemberSaveRequest requestDto);

    Page<MemberResponse> members(String name, Pageable pageable);

    MemberResponse member(Long id);

    MemberResponse updateMember(Long id, MemberUpdateRequest requestDto);

    void deleteMember(Long id);
}
