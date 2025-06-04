package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.infrastructure.persistence.jpa.TicketJpaRepository;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

	private final TicketJpaRepository jpaRepository;

	@Override
	public Optional<Ticket> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Ticket> findByProductId(Long productId) {
		return jpaRepository.findByProductId(productId);
	}

	@Override
	public Ticket save(Ticket ticket) {
		return jpaRepository.save(ticket);
	}

	@Override
	public void delete(Ticket ticket) {
		jpaRepository.delete(ticket);
	}
}