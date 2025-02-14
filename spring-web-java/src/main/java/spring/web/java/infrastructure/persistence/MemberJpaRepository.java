package spring.web.java.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.web.java.domain.model.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		FROM Member m
		WHERE m.account = :account
		""")
	Optional<Member> findByAccount(String account);
}
