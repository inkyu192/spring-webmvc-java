package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.vo.Email;

public interface MemberRepository {

	Optional<Member> findById(Long id);

	Optional<Member> findByEmail(Email email);

	boolean existsByEmail(Email email);

	Member save(Member member);

	void delete(Member member);
}
