package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByAccount(String account);

	boolean existsByAccount(String account);
}
