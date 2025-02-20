package spring.web.java.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.event.MemberEvent;
import spring.web.java.domain.model.entity.Address;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.infrastructure.util.SecurityContextUtil;
import spring.web.java.presentation.dto.request.MemberSaveRequest;
import spring.web.java.presentation.dto.request.MemberUpdateRequest;
import spring.web.java.presentation.dto.response.MemberResponse;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
		memberRepository.findByAccount(memberSaveRequest.account())
			.ifPresent(member -> {
				throw new BaseException(ErrorCode.DUPLICATE_DATA, HttpStatus.CONFLICT);
			});

		Member member = memberRepository.save(
			Member.create(
				memberSaveRequest.account(),
				passwordEncoder.encode(memberSaveRequest.password()),
				memberSaveRequest.name(),
				memberSaveRequest.role(),
				Address.create(
					memberSaveRequest.city(),
					memberSaveRequest.street(),
					memberSaveRequest.zipcode()
				)
			)
		);

		eventPublisher.publishEvent(new MemberEvent(member.getAccount(), member.getName()));

		return new MemberResponse(member);
	}

	public MemberResponse findMember() {
		return memberRepository.findById(SecurityContextUtil.getMemberId())
			.map(MemberResponse::new)
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	@Transactional
	public MemberResponse updateMember(MemberUpdateRequest memberUpdateRequest) {
		Member member = memberRepository.findById(SecurityContextUtil.getMemberId())
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

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
	public void deleteMember() {
		memberRepository.deleteById(SecurityContextUtil.getMemberId());
	}
}
