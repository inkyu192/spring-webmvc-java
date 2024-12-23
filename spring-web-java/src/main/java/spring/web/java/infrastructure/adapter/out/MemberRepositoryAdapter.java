package spring.web.java.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.MemberJpaRepositoryPort;
import spring.web.java.application.port.out.MemberRepositoryPort;
import spring.web.java.domain.Member;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepositoryPort {

	private final MemberJpaRepositoryPort jpaRepository;

	@Override
	public Optional<Member> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Member> findByAccount(String account) {
		return jpaRepository.findByAccount(account);
	}

	@Override
	public Member save(Member member) {
		return jpaRepository.save(member);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
