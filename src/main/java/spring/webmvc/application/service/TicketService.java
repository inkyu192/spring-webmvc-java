package spring.webmvc.application.service;

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
	public void deleteTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticketRepository.delete(ticket);
	}
}
