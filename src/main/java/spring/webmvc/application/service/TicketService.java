package spring.webmvc.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;

	@Transactional
	public Ticket createTicket(
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		return ticketRepository.save(
			Ticket.create(
				name,
				description,
				price,
				quantity,
				place,
				performanceTime,
				duration,
				ageLimit
			)
		);
	}

	@Transactional
	public Ticket updateTicket(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticket.update(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		return ticket;
	}

	@Transactional
	public void deleteTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticketRepository.delete(ticket);
	}
}
