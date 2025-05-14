package spring.webmvc.application.service;

import java.time.LocalDate;
import java.util.List;
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
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.exception.DuplicateEntityException;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	private final PermissionService permissionService;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public Member createMember(
		String account,
		String password,
		String name,
		String phone,
		LocalDate birthDate,
		List<Long> roleIds,
		List<Long> permissionIds
	) {
		if (memberRepository.existsByAccount(account)) {
			throw new DuplicateEntityException(Member.class, account);
		}

		Member member = Member.create(account, passwordEncoder.encode(password), name, phone, birthDate);

		if (!ObjectUtils.isEmpty(roleIds)) {
			Map<Long, Role> roleMap = roleRepository.findAllById(roleIds).stream()
				.collect(Collectors.toMap(Role::getId, role -> role));

			for (Long id : roleIds) {
				Role role = roleMap.get(id);
				if (role == null) {
					throw new EntityNotFoundException(Role.class, id);
				}
				member.addRole(role);
			}
		}

		if (!ObjectUtils.isEmpty(permissionIds)) {
			permissionService.addPermission(permissionIds, member::addPermission);
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

		return member;
	}

	public Member findMember() {
		Long memberId = SecurityContextUtil.getMemberId();

		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
	}

	@Transactional
	public Member updateMember(String password, String name, String phone, LocalDate birthDate) {
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		member.update(password, name, phone, birthDate);

		return member;
	}

	@Transactional
	public void deleteMember() {
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		memberRepository.delete(member);
	}
}
