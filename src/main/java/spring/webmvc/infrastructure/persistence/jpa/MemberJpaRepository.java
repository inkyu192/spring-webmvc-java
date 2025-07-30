package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.vo.Email;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(Email email);

	boolean existsByEmail(Email email);
}
