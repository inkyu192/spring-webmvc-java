package spring.webmvc.application.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PermissionService permissionService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			1L,
			null,
			List.of(new SimpleGrantedAuthority("TEST"))
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@AfterEach
	void afterEach() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("findMember: Member 없을 경우 EntityNotFoundException 발생한다")
	void findMemberCase1() {
		Long memberId = 1L;

		Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> memberService.findMember())
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("findMember: Member 있을 경우 조회 후 반환한다")
	void findMemberCase2() {
		Long memberId = 1L;

		Member member = Mockito.mock(Member.class);
		Mockito.when(member.getId()).thenReturn(memberId);
		Mockito.when(member.getAccount()).thenReturn("test@gmail.com");
		Mockito.when(member.getName()).thenReturn("name");
		Mockito.when(member.getPhone()).thenReturn("010-1234-1234");
		Mockito.when(member.getBirthDate()).thenReturn(LocalDate.now());
		Mockito.when(member.getCreatedAt()).thenReturn(Instant.now());

		Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

		Member result = memberService.findMember();

		Assertions.assertThat(result.getId()).isEqualTo(member.getId());
		Assertions.assertThat(result.getAccount()).isEqualTo(member.getAccount());
		Assertions.assertThat(result.getName()).isEqualTo(member.getName());
		Assertions.assertThat(result.getPhone()).isEqualTo(member.getPhone());
		Assertions.assertThat(result.getBirthDate()).isEqualTo(member.getBirthDate());
		Assertions.assertThat(result.getCreatedAt()).isEqualTo(member.getCreatedAt());
	}
}
