package spring.web.java.domain.repository;

import java.util.Optional;

import spring.web.java.domain.model.entity.Member;

public interface MemberRepository {

	Optional<Member> findById(Long id);

	Optional<Member> findByAccount(String account);

	Member save(Member member);

	void delete(Member member);
}
