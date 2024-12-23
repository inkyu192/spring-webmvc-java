package spring.web.java.application.port.out;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.web.java.domain.Member;

public interface MemberJpaRepositoryPort extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		FROM Member m
		WHERE m.account = :account
		""")
	Optional<Member> findByAccount(String account);
}
