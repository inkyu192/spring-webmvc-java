package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.infrastructure.persistence.TicketJpaRepository;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

	private final TicketJpaRepository jpaRepository;

	@Override
	public Optional<Ticket> findById(Long id) {
		return jpaRepository.findById(id);
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