package spring.webmvc.application.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.NotificationEvent;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.infrastructure.util.SecurityContextUtil;
import spring.webmvc.presentation.dto.request.MemberSaveRequest;
import spring.webmvc.presentation.dto.request.MemberUpdateRequest;
import spring.webmvc.presentation.dto.response.MemberResponse;
import spring.webmvc.presentation.exception.DuplicateEntityException;
import spring.webmvc.presentation.exception.EntityNotFoundException;

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
		if (memberRepository.existsByAccount(memberSaveRequest.account())) {
			throw new DuplicateEntityException(Member.class, memberSaveRequest.account());
		}

		Member member = Member.create(
			memberSaveRequest.account(),
			passwordEncoder.encode(memberSaveRequest.password()),
			memberSaveRequest.name(),
			memberSaveRequest.phone(),
			memberSaveRequest.birthDate()
		);

		if (!ObjectUtils.isEmpty(memberSaveRequest.roleIds())) {
			Map<Long, Role> roleMap = roleRepository.findAllById(memberSaveRequest.roleIds()).stream()
				.collect(Collectors.toMap(Role::getId, role -> role));

			for (Long id : memberSaveRequest.roleIds()) {
				Role role = roleMap.get(id);
				if (role == null) {
					throw new EntityNotFoundException(Role.class, id);
				}
				member.addRole(role);
			}
		}

		if (!ObjectUtils.isEmpty(memberSaveRequest.permissionIds())) {
			Map<Long, Permission> permissionMap = permissionRepository.findAllById(memberSaveRequest.permissionIds())
				.stream()
				.collect(Collectors.toMap(Permission::getId, permission -> permission));

			for (Long id : memberSaveRequest.permissionIds()) {
				Permission permission = permissionMap.get(id);
				if (permission == null) {
					throw new EntityNotFoundException(Permission.class, id);
				}
				member.addPermission(permission);
			}
		}

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
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		return new MemberResponse(member);
	}

	@Transactional
	public MemberResponse updateMember(MemberUpdateRequest memberUpdateRequest) {
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		member.update(
			memberUpdateRequest.password(),
			memberUpdateRequest.name(),
			memberUpdateRequest.phone(),
			memberUpdateRequest.birthDate()
		);

		return new MemberResponse(member);
	}

	@Transactional
	public void deleteMember() {
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		memberRepository.delete(member);
	}
}
