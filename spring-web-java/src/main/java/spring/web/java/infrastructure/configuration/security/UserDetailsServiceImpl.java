package spring.web.java.infrastructure.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.MemberJpaRepositoryPort;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberJpaRepositoryPort memberJpaRepositoryPort;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberJpaRepositoryPort.findByAccount(username)
			.map(UserDetailsImpl::new)
			.orElseThrow(() -> new UsernameNotFoundException("유저 없음"));
	}
}
