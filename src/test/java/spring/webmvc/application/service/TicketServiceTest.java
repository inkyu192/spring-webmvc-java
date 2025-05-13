package spring.webmvc.application.service;

import java.time.Instant;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@InjectMocks
	private TicketService ticketService;

	@Mock
	private TicketRepository ticketRepository;

	@Test
	@DisplayName("deleteTicket: Ticket 없을 경우 EntityNotFoundException 발생한다")
	void deleteTicketCase1() {
		// Given
		Long ticketId = 1L;

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> ticketService.deleteTicket(ticketId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("deleteTicket: Ticket 있을 경우 삭제한다")
	void deleteTicketCase2() {
		// Given
		Long ticketId = 1L;
		Ticket ticket = Ticket.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			"duration",
			"ageLimit"
		);

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

		// When
		ticketService.deleteTicket(ticketId);

		// Then
		Mockito.verify(ticketRepository, Mockito.times(1)).delete(ticket);
	}
}
