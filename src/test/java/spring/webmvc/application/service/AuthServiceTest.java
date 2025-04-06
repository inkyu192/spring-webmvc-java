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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.TokenRepository;
import spring.webmvc.infrastructure.config.security.JwtTokenProvider;
import spring.webmvc.presentation.dto.request.MemberLoginRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.TokenResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("login 은 계정이 존재하지 않을 경우 BadCredentialsException 던진다")
	void login_case1() {
		// Given
		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);

		Mockito.when(memberRepository.findByAccount(request.account())).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login 은 비밀번호가 일치하지 않을 경우 BadCredentialsException 던진다")
	void login_case2() {
		// Given
		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(request.account())).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login 은 비밀번호가 일치할 경우 토큰을 발급한다")
	void login_case3() {
		// Given
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(Mockito.any())).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(jwtTokenProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn(accessToken);
		Mockito.when(jwtTokenProvider.createRefreshToken()).thenReturn(refreshToken);
		Mockito.when(tokenRepository.save(Mockito.any(), Mockito.any())).thenReturn(refreshToken);

		// When
		TokenResponse response = authService.login(request);

		// Then
		Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(), Mockito.any());
		Assertions.assertThat(response.accessToken()).isEqualTo(accessToken);
		Assertions.assertThat(response.refreshToken()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("refreshToken 은 Access 토큰이 유효하지 않을 경우 JwtException 던진다")
	void refreshToken_case1() {
		// Given
		TokenRequest request = Mockito.mock(TokenRequest.class);

		Mockito.when(jwtTokenProvider.parseAccessToken(request.accessToken())).thenThrow(JwtException.class);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request)).isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken 은 Refresh 토큰이 유효하지 않을 경우 JwtException 던진다")
	void refreshToken_case2() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "fakeRefreshToken");
		Claims claims = Mockito.mock(Claims.class);

		Mockito.when(jwtTokenProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.doThrow(JwtException.class).when(jwtTokenProvider).validateRefreshToken(Mockito.any());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request)).isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken 은 Refresh 토큰이 일치하지 않을 경우 BadCredentialsException 던진다")
	void refreshToken_case3() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "fakeRefreshToken");
		Claims claims = Mockito.mock(Claims.class);
		String token = "refreshToken";

		Mockito.when(jwtTokenProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(token));

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request))
			.isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("refreshToken 은 토큰이 유효할 경우 Access 토큰이 갱신된다")
	void refreshToken_case4() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "refreshToken");
		String token = "refreshToken";

		Claims claims = Mockito.mock(Claims.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(jwtTokenProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(token));
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		Mockito.when(jwtTokenProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn("newAccessToken");

		// When
		TokenResponse response = authService.refreshToken(request);

		// Then
		Assertions.assertThat(response.accessToken()).isNotEqualTo(request.accessToken());
		Assertions.assertThat(response.refreshToken()).isEqualTo(request.refreshToken());
	}
}
