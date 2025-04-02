package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.infrastructure.persistence.MemberJpaRepository;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

	private final MemberJpaRepository jpaRepository;

	@Override
	public Optional<Member> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Member> findByAccount(String account) {
		return jpaRepository.findByAccount(account);
	}

	@Override
	public boolean existsByAccount(String account) {
		return jpaRepository.existsByAccount(account);
	}

	@Override
	public Member save(Member member) {
		return jpaRepository.save(member);
	}

	@Override
	public void delete(Member member) {
		jpaRepository.delete(member);
	}
}
