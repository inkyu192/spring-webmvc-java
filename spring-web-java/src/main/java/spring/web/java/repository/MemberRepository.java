package spring.web.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.web.java.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """)
    Optional<Member> findByAccount(String account);
}
