package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.presentation.dto.request.TicketCreateRequest;
import spring.webmvc.presentation.dto.request.TicketUpdateRequest;
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
	public TicketResponse createTicket(TicketCreateRequest ticketCreateRequest) {
		Ticket ticket = ticketRepository.save(
			Ticket.create(
				ticketCreateRequest.name(),
				ticketCreateRequest.description(),
				ticketCreateRequest.price(),
				ticketCreateRequest.quantity(),
				ticketCreateRequest.place(),
				ticketCreateRequest.performanceTime(),
				ticketCreateRequest.duration(),
				ticketCreateRequest.ageLimit()
			)
		);

		return new TicketResponse(ticket);
	}

	@Transactional
	public TicketResponse updateTicket(Long id, TicketUpdateRequest ticketUpdateRequest) {
		Ticket ticket = ticketRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		ticket.update(
			ticketUpdateRequest.name(),
			ticketUpdateRequest.description(),
			ticketUpdateRequest.price(),
			ticketUpdateRequest.quantity(),
			ticketUpdateRequest.place(),
			ticketUpdateRequest.performanceTime(),
			ticketUpdateRequest.duration(),
			ticketUpdateRequest.ageLimit()
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
