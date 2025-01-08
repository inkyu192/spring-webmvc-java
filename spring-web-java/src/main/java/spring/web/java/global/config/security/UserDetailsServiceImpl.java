package spring.web.java.global.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByAccount(username)
			.map(UserDetailsImpl::new)
			.orElseThrow(() -> new UsernameNotFoundException("유저 없음"));
	}
}
