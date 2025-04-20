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
import spring.webmvc.infrastructure.config.security.JwtProvider;
import spring.webmvc.presentation.dto.request.MemberLoginRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.TokenResponse;

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
		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);

		Mockito.when(memberRepository.findByAccount(request.account())).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login: 비밀번호가 일치하지 않을 경우 BadCredentialsException 발생한다")
	void loginCase2() {
		// Given
		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(request.account())).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("login: 유효성 검사 성공할 경우 Token 저장하고 반환한다")
	void loginCase3() {
		// Given
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		MemberLoginRequest request = Mockito.mock(MemberLoginRequest.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberRepository.findByAccount(Mockito.any())).thenReturn(Optional.of(member));
		Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(jwtProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn(accessToken);
		Mockito.when(jwtProvider.createRefreshToken()).thenReturn(refreshToken);
		Mockito.when(tokenRepository.save(Mockito.any(), Mockito.any())).thenReturn(refreshToken);

		// When
		TokenResponse response = authService.login(request);

		// Then
		Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(), Mockito.any());
		Assertions.assertThat(response.accessToken()).isEqualTo(accessToken);
		Assertions.assertThat(response.refreshToken()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("refreshToken: AccessToken 유효하지 않을 경우 JwtException 발생한다")
	void refreshTokenCase1() {
		// Given
		TokenRequest request = Mockito.mock(TokenRequest.class);

		Mockito.when(jwtProvider.parseAccessToken(request.accessToken())).thenThrow(JwtException.class);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request)).isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken: RefreshToken 유효하지 않을 경우 JwtException 발생한다")
	void refreshTokenCase2() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "fakeRefreshToken");
		Claims claims = Mockito.mock(Claims.class);

		Mockito.when(jwtProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.doThrow(JwtException.class).when(jwtProvider).validateRefreshToken(Mockito.any());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request)).isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("refreshToken: RefreshToken 일치하지 않을 경우 BadCredentialsException 발생한다")
	void refreshTokenCase3() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "fakeRefreshToken");
		Claims claims = Mockito.mock(Claims.class);
		String token = "refreshToken";

		Mockito.when(jwtProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(token));

		// When & Then
		Assertions.assertThatThrownBy(() -> authService.refreshToken(request))
			.isInstanceOf(BadCredentialsException.class);
	}

	@Test
	@DisplayName("refreshToken: 유효성 검사 성공할 경우 AccessToken 갱신된다")
	void refreshTokenCase4() {
		// Given
		Long memberId = 1L;
		TokenRequest request = new TokenRequest("accessToken", "refreshToken");
		String token = "refreshToken";

		Claims claims = Mockito.mock(Claims.class);
		Member member = Mockito.mock(Member.class);

		Mockito.when(jwtProvider.parseAccessToken(request.accessToken())).thenReturn(claims);
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Mockito.mock());
		Mockito.when(claims.get("memberId")).thenReturn(memberId);
		Mockito.when(tokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(token));
		Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		Mockito.when(jwtProvider.createAccessToken(Mockito.any(), Mockito.any())).thenReturn("newAccessToken");

		// When
		TokenResponse response = authService.refreshToken(request);

		// Then
		Assertions.assertThat(response.accessToken()).isNotEqualTo(request.accessToken());
		Assertions.assertThat(response.refreshToken()).isEqualTo(request.refreshToken());
	}
}
