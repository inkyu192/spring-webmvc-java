package spring.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.domain.Address;
import spring.web.java.domain.Member;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.exception.CommonException;
import spring.web.java.repository.MemberRepository;


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

        return new MemberResponse(member);
    }

    public Page<MemberResponse> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAllWithJpql(pageable, account, name)
                .map(MemberResponse::new);
    }

    public MemberResponse findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::new)
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

        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
