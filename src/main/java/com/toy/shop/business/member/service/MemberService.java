package com.toy.shop.business.member.service;

import com.toy.shop.business.member.dto.request.MemberSaveRequest;
import com.toy.shop.business.member.dto.request.MemberUpdateRequest;
import com.toy.shop.business.member.dto.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    MemberResponse saveMember(MemberSaveRequest memberSaveRequest);

    Page<MemberResponse> members(String name, Pageable pageable);

    MemberResponse member(Long id);

    MemberResponse updateMember(Long id, MemberUpdateRequest requestDto);

    void deleteMember(Long id);
}
