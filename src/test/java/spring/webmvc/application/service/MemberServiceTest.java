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
	private PermissionRepository permissionRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			1L,
			null,
			List.of(new SimpleGrantedAuthority("ROLE_USER"))
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@AfterEach
	void afterEach() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("엔티티가 존재하지 않을 경우 예외를 던진다")
	void case1() {
		Mockito.when(memberRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> memberService.findMember())
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("엔티티가 존재할 경우 MemberResponse 반환한다")
	void case2() {
		Long memberId = 1L;

		Member member = Mockito.mock(Member.class);
		Mockito.when(member.getId()).thenReturn(memberId);
		Mockito.when(member.getAccount()).thenReturn("test@gmail.com");
		Mockito.when(member.getName()).thenReturn("name");
		Mockito.when(member.getPhone()).thenReturn("010-1234-1234");
		Mockito.when(member.getBirthDate()).thenReturn(LocalDate.now());
		Mockito.when(member.getCreatedAt()).thenReturn(Instant.now());

		Mockito.when(memberRepository.findById(Mockito.any())).thenReturn(Optional.of(member));

		var result = memberService.findMember();

		Assertions.assertThat(result.id()).isEqualTo(memberId);
	}
}