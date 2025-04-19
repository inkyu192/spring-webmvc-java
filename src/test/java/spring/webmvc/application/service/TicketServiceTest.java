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
import spring.webmvc.presentation.dto.request.TicketSaveRequest;
import spring.webmvc.presentation.dto.response.TicketResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@InjectMocks
	private TicketService ticketService;

	@Mock
	private TicketRepository ticketRepository;

	@Test
	@DisplayName("createTicket은 티켓을 생성한다")
	void createTicket_case1() {
		// Given
		TicketSaveRequest request = new TicketSaveRequest(
			"Concert Ticket",
			"A great concert",
			1000,
			5,
			"Concert Hall",
			Instant.now(),
			"2 hours",
			"All ages"
		);
		Ticket ticket = Ticket.create("Concert Ticket", "A great concert", 1000, 5,
			"Concert Hall", Instant.now(), "2 hours", "All ages");

		Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket);

		// When
		TicketResponse response = ticketService.createTicket(request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
		Assertions.assertThat(request.place()).isEqualTo(response.place());
		Assertions.assertThat(request.duration()).isEqualTo(response.duration());
		Assertions.assertThat(request.ageLimit()).isEqualTo(response.ageLimit());
	}

	@Test
	@DisplayName("findTicket은 데이터가 있을 경우 조회한다")
	void findTicket_case1() {
		// Given
		Long ticketId = 1L;
		Ticket ticket = Ticket.create("Concert Ticket", "A great concert", 1000, 5,
			"Concert Hall", Instant.now(), "2 hours", "All ages");

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

		// When
		TicketResponse response = ticketService.findTicket(ticketId);

		// Then
		Assertions.assertThat(ticket.getProduct().getName()).isEqualTo(response.name());
		Assertions.assertThat(ticket.getProduct().getDescription()).isEqualTo(response.description());
		Assertions.assertThat(ticket.getProduct().getPrice()).isEqualTo(response.price());
		Assertions.assertThat(ticket.getProduct().getQuantity()).isEqualTo(response.quantity());
		Assertions.assertThat(ticket.getPlace()).isEqualTo(response.place());
		Assertions.assertThat(ticket.getDuration()).isEqualTo(response.duration());
		Assertions.assertThat(ticket.getAgeLimit()).isEqualTo(response.ageLimit());
	}

	@Test
	@DisplayName("findTicket은 데이터가 없을 경우 EntityNotFoundException을 던진다")
	void findTicket_case2() {
		// Given
		Long ticketId = 1L;

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> ticketService.findTicket(ticketId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateTicket은 티켓을 수정한다")
	void updateTicket_case2() {
		// Given
		Long ticketId = 1L;
		Ticket oldTicket = Ticket.create(
			"Old Concert",
			"Old description",
			1000,
			5,
			"Old Hall",
			Instant.now(),
			"2 hours",
			"All ages"
		);
		TicketSaveRequest request = new TicketSaveRequest(
			"Concert Ticket",
			"A great concert",
			1000,
			5,
			"Concert Hall",
			Instant.now(),
			"2 hours",
			"All ages"
		);

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(oldTicket));

		// When
		TicketResponse response = ticketService.updateTicket(ticketId, request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
		Assertions.assertThat(request.place()).isEqualTo(response.place());
		Assertions.assertThat(request.duration()).isEqualTo(response.duration());
		Assertions.assertThat(request.ageLimit()).isEqualTo(response.ageLimit());

		Mockito.verify(ticketRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("deleteTicket은 데이터가 없을 경우 EntityNotFoundException을 던진다")
	void deleteTicket_case1() {
		// Given
		Long ticketId = 1L;

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> ticketService.deleteTicket(ticketId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("deleteTicket은 데이터가 있을 경우 삭제한다")
	void deleteTicket_case2() {
		// Given
		Long ticketId = 1L;
		Ticket ticket = Ticket.create(
			"Old Concert",
			"Old description",
			1000,
			5,
			"Old Hall",
			Instant.now(),
			"2 hours",
			"All ages"
		);

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

		// When
		ticketService.deleteTicket(ticketId);

		// Then
		Mockito.verify(ticketRepository, Mockito.times(1)).delete(ticket);
	}
}