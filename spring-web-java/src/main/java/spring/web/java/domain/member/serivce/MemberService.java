package spring.web.java.domain.member.serivce;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Address;
import spring.web.java.domain.member.Member;
import spring.web.java.domain.member.dto.MemberLoginRequest;
import spring.web.java.domain.member.repository.MemberRepository;
import spring.web.java.domain.token.Token;
import spring.web.java.domain.token.repository.TokenRepository;
import spring.web.java.domain.member.dto.MemberSaveRequest;
import spring.web.java.domain.member.dto.MemberUpdateRequest;
import spring.web.java.domain.member.dto.MemberResponse;
import spring.web.java.domain.token.dto.TokenResponse;
import spring.web.java.global.common.JwtTokenProvider;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.config.security.UserDetailsImpl;
import spring.web.java.global.exception.DomainException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

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

	@Transactional
	public TokenResponse login(MemberLoginRequest memberLoginRequest) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(memberLoginRequest.account(), memberLoginRequest.password());

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

	public MemberResponse findMember(Long id) {
		return memberRepository.findById(id)
			.map(MemberResponse::new)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

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

	@Transactional
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
}
