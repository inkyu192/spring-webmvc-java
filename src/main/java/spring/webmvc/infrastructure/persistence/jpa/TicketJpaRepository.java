package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Ticket;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findByProductId(Long productId);
}