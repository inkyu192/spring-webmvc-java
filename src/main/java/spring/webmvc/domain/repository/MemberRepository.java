package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Member;

public interface MemberRepository {

	Optional<Member> findById(Long id);

	Optional<Member> findByAccount(String account);

	boolean existsByAccount(String account);

	Member save(Member member);

	void delete(Member member);
}
