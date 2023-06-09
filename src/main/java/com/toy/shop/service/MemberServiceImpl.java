package com.toy.shop.service;

import com.toy.shop.domain.Member;
import com.toy.shop.dto.MemberDto;
import com.toy.shop.exception.CommonException;
import com.toy.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<MemberDto.Response> members(String name) {
        List<Member> members = memberRepository.findAllOfQueryMethod(name);

        return members.stream()
                .map(MemberDto.Response::new)
                .toList();
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
