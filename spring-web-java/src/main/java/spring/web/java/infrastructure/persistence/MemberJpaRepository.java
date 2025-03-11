package spring.web.java.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByAccount(String account);

	boolean existsByAccount(String account);
}
