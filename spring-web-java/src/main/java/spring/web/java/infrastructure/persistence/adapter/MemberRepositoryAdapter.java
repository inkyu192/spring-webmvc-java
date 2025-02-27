package spring.web.java.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.infrastructure.persistence.MemberJpaRepository;

@Repository
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
	public Member save(Member member) {
		return jpaRepository.save(member);
	}

	@Override
	public void delete(Member member) {
		jpaRepository.delete(member);
	}
}
