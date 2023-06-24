package com.toy.shop.service;

import com.toy.shop.domain.Member;
import com.toy.shop.dto.MemberDto;
import com.toy.shop.exception.CommonException;
import com.toy.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.toy.shop.common.ResultCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberDto.Response saveMember(MemberDto.Save requestDto) {
        Member member = Member.createMember(requestDto);

        memberRepository.save(member);

        return new MemberDto.Response(member);
    }

    @Override
    public Page<MemberDto.Response> members(String name, Pageable pageable) {
        Page<Member> page = memberRepository.findAllOfQueryMethod(name, pageable);

        return page.map(MemberDto.Response::new);
    }

    @Override
    public MemberDto.Response member(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        return new MemberDto.Response(member);
    }

    @Override
    @Transactional
    public MemberDto.Response updateMember(Long id, MemberDto.Update requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        member.updateMember(requestDto);

        return new MemberDto.Response(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }
}
