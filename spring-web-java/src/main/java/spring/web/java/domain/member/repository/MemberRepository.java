package spring.web.java.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.web.java.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("""
		SELECT m
		FROM Member m
		WHERE m.account = :account
		""")
	Optional<Member> findByAccount(String account);
}
