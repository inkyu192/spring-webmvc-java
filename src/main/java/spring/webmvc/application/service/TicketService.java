package spring.webmvc.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.TicketDto;
import spring.webmvc.domain.cache.TicketCache;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;
	private final TicketCache ticketCache;
	private final JsonSupport jsonSupport;

	public TicketDto findTicket(Long id) {
		Optional<TicketDto> cached = ticketCache.get(id)
			.flatMap(value -> jsonSupport.readValue(value, TicketDto.class));

		if (cached.isPresent()) {
			return cached.get();
		}

		TicketDto ticketDto = ticketRepository.findById(id)
			.map(TicketDto::new)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, id));

		jsonSupport.writeValueAsString(ticketDto).ifPresent(value -> ticketCache.set(id, value));

		return ticketDto;
	}

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
