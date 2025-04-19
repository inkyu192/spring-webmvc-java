package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Ticket;

public interface TicketRepository {

	Optional<Ticket> findById(Long id);

	Ticket save(Ticket ticket);

	void delete(Ticket ticket);
}