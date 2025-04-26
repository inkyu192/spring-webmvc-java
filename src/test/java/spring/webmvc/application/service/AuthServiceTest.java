package spring.webmvc.application.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.TokenRepository;
import spring.webmvc.infrastructure.config.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("login: Member 엔티티 없을 경우 BadCredentialsException 발생한다")
	void loginCase1() {
		// Given
		String account = "account";
		String password = "password";

		Mockito.when(memberRepository.findByAccount(account)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(account, password))
			.isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login: 비밀번호가 일치하지 않을 경우 BadCredentialsException 발생한다")
	void loginCase2() {
		// Given
		String account = "account";
		String fakePassword = "fakePassword";
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(account)).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(fakePassword, member.getPassword())).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(account, fakePassword))
			.isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login: 유효성 검사 성공할 경우 Token 저장 후 반환한다")
	void loginCase3() {
		// Given
		String account = "account";
		String password = "password";
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(account)).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(jwtProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn(accessToken);
		Mockito.when(jwtProvider.createRefreshToken()).thenReturn(refreshToken);
		Mockito.when(tokenRepository.save(Mockito.any(), Mockito.any())).thenReturn(refreshToken);

		// When
		Pair<String, String> pair = authService.login(account, password);

		// Then
		Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(), Mockito.any());
		Assertions.assertThat(pair.getFirst()).isEqualTo(accessToken);
		Assertions.assertThat(pair.getSecond()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("refreshToken: accessToken 유효하지 않을 경우 JwtException 발생한다")
	void refreshTokenCase1() {
		// Given
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		Mockito.when(jwtProvider.parseAccessToken(accessToken)).thenThrow(JwtException.class);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(accessToken, refreshToken))
			.isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken: refreshToken 유효하지 않을 경우 JwtException 발생한다")
	void refreshTokenCase2() {
		// Given
		Long memberId = 1L;
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		Claims claims = Mockito.mock(Claims.class);

		Mockito.when(jwtProvider.parseAccessToken(accessToken)).thenReturn(claims);
		Mockito.doThrow(JwtException.class).when(jwtProvider).validateRefreshToken(refreshToken);
		Mockito.when(claims.get("memberId")).thenReturn(memberId);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(accessToken, refreshToken))
			.isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken: refreshToken 일치하지 않을 경우 BadCredentialsException 발생한다")
	void refreshTokenCase3() {
		// Given
		Long memberId = 1L;
		String accessToken = "accessToken";
		String fakeRefreshToken = "fakeRefreshToken";
		String refreshToken = "refreshToken";
		Claims claims = Mockito.mock(Claims.class);

		Mockito.when(jwtProvider.parseAccessToken(accessToken)).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(refreshToken));

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(accessToken, fakeRefreshToken))
			.isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("refreshToken: 유효성 검사 성공할 경우 AccessToken 갱신된다")
	void refreshTokenCase4() {
		// Given
		Long memberId = 1L;
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		Claims claims = Mockito.mock(Claims.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(jwtProvider.parseAccessToken(accessToken)).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(refreshToken));
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		Mockito.when(jwtProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn("newAccessToken");

		// When
		Pair<String, String> pair = authService.refreshToken(accessToken, refreshToken);

		// Then
		Assertions.assertThat(pair.getFirst()).isNotEqualTo(accessToken);
		Assertions.assertThat(pair.getSecond()).isEqualTo(refreshToken);
	}
}
