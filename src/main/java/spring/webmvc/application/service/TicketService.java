package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.presentation.dto.request.TicketSaveRequest;
import spring.webmvc.presentation.dto.response.TicketResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;

	public TicketResponse findTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		return new TicketResponse(ticket);
	}

	@Transactional
	public TicketResponse createTicket(TicketSaveRequest ticketSaveRequest) {
		Ticket ticket = ticketRepository.save(
			Ticket.create(
				ticketSaveRequest.name(),
				ticketSaveRequest.description(),
				ticketSaveRequest.price(),
				ticketSaveRequest.quantity(),
				ticketSaveRequest.place(),
				ticketSaveRequest.performanceTime(),
				ticketSaveRequest.duration(),
				ticketSaveRequest.ageLimit()
			)
		);

		return new TicketResponse(ticket);
	}

	@Transactional
	public TicketResponse updateTicket(Long id, TicketSaveRequest ticketSaveRequest) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticket.update(
			ticketSaveRequest.name(),
			ticketSaveRequest.description(),
			ticketSaveRequest.price(),
			ticketSaveRequest.quantity(),
			ticketSaveRequest.place(),
			ticketSaveRequest.performanceTime(),
			ticketSaveRequest.duration(),
			ticketSaveRequest.ageLimit()
		);

		return new TicketResponse(ticket);
	}

	@Transactional
	public void deleteTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticketRepository.delete(ticket);
	}
}
