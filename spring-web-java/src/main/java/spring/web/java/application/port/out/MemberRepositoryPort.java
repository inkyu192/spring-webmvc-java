package spring.web.java.application.port.out;

import java.util.Optional;

import spring.web.java.domain.Member;

public interface MemberRepositoryPort {

	Optional<Member> findById(Long id);

	Optional<Member> findByAccount(String account);

	Member save(Member member);

	void deleteById(Long id);
}
