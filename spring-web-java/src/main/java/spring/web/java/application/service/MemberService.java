package spring.web.java.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.event.NotificationEvent;
import spring.web.java.domain.model.entity.Address;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.entity.MemberPermission;
import spring.web.java.domain.model.entity.MemberRole;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.Role;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.domain.repository.PermissionRepository;
import spring.web.java.domain.repository.RoleRepository;
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
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
		List<Long> roleIds = memberSaveRequest.roleIds();
		List<Long> permissionIds = memberSaveRequest.permissionIds();

		if (ObjectUtils.isEmpty(roleIds) && ObjectUtils.isEmpty(permissionIds)) {
			throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, HttpStatus.BAD_REQUEST);
		}

		memberRepository.findByAccount(memberSaveRequest.account()).ifPresent(member -> {
			throw new BaseException(ErrorCode.DUPLICATE_DATA, HttpStatus.CONFLICT);
		});

		Address address = Address.create(
			memberSaveRequest.city(),
			memberSaveRequest.street(),
			memberSaveRequest.zipcode()
		);

		List<MemberRole> memberRoles = new ArrayList<>();
		if (!ObjectUtils.isEmpty(roleIds)) {
			roleIds.forEach(id -> {
				Role role = roleRepository.findById(id)
					.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

				memberRoles.add(MemberRole.create(role));
			});
		}

		List<MemberPermission> memberPermissions = new ArrayList<>();
		if (!ObjectUtils.isEmpty(permissionIds)) {
			permissionIds.forEach(id -> {
				Permission permission = permissionRepository.findById(id)
					.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

				memberPermissions.add(MemberPermission.create(permission));
			});
		}

		Member member = Member.create(
			memberSaveRequest.account(),
			passwordEncoder.encode(memberSaveRequest.password()),
			memberSaveRequest.name(),
			address,
			memberRoles,
			memberPermissions
		);

		memberRepository.save(member);

		eventPublisher.publishEvent(
			new NotificationEvent(
				member.getId(),
				"회원가입 완료",
				"회원가입을 환영합니다!",
				"/test/123"
			)
		);

		return new MemberResponse(member);
	}

	public MemberResponse findMember() {
		Member member = memberRepository.findById(SecurityContextUtil.getMemberId())
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		return new MemberResponse(member);
	}

	@Transactional
	public MemberResponse updateMember(MemberUpdateRequest memberUpdateRequest) {
		Member member = memberRepository.findById(SecurityContextUtil.getMemberId())
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		member.update(
			memberUpdateRequest.name(),
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
		Member member = memberRepository.findById(SecurityContextUtil.getMemberId())
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		memberRepository.delete(member);
	}
}
