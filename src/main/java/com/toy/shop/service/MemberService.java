package com.toy.shop.service;

import com.toy.shop.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    MemberDto.Response saveMember(MemberDto.Save requestDto);

    Page<MemberDto.Response> members(String name, Pageable pageable);

    MemberDto.Response member(Long id);

    MemberDto.Response updateMember(Long id, MemberDto.Update requestDto);

    void deleteMember(Long id);
}
