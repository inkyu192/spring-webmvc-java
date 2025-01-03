package spring.web.java.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.MemberServicePort;
import spring.web.java.application.port.out.MemberRepositoryPort;
import spring.web.java.application.port.out.TokenRepositoryPort;
import spring.web.java.common.JwtTokenProvider;
import spring.web.java.common.ResponseMessage;
import spring.web.java.domain.Address;
import spring.web.java.domain.Member;
import spring.web.java.domain.Token;
import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.infrastructure.configuration.exception.DomainException;
import spring.web.java.infrastructure.configuration.security.UserDetailsImpl;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements MemberServicePort {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenRepositoryPort tokenRepository;
	private final MemberRepositoryPort memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public MemberResponse saveMember(MemberSaveRequest memberSaveRequest) {
		memberRepository.findByAccount(memberSaveRequest.account())
			.ifPresent(member -> {
				throw new DomainException(ResponseMessage.DUPLICATE_DATA, HttpStatus.CONFLICT);
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

		return new MemberResponse(memberRepository.save(member));
	}

	@Override
	@Transactional
	public TokenResponse login(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

		Authentication authentication;

		try {
			authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (BadCredentialsException e) {
			throw new DomainException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED);
		}

		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

		String accessToken = jwtTokenProvider.createAccessToken(userDetails.getMemberId(), userDetails.getRole());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		tokenRepository.save(Token.create(userDetails.getMemberId(), refreshToken));

		return new TokenResponse(accessToken, refreshToken);
	}

	@Override
	public MemberResponse findMember(Long id) {
		return memberRepository.findById(id)
			.map(MemberResponse::new)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

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

	@Override
	@Transactional
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
}
