package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.infrastructure.persistence.jpa.MemberJpaRepository;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

	private final MemberJpaRepository jpaRepository;

	@Override
	public Optional<Member> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Member> findByEmail(Email email) {
		return jpaRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(Email email) {
		return jpaRepository.existsByEmail(email);
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
