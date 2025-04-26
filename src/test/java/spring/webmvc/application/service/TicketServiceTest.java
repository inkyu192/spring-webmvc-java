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
	@DisplayName("createTicket: Ticket 저장 후 반환한다")
	void createTicket() {
		// Given
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";

		Ticket ticket = Ticket.create(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket);

		// When
		Ticket result = ticketService.createTicket(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		// Then
		Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(place).isEqualTo(result.getPlace());
		Assertions.assertThat(performanceTime).isEqualTo(result.getPerformanceTime());
		Assertions.assertThat(duration).isEqualTo(result.getDuration());
		Assertions.assertThat(ageLimit).isEqualTo(result.getAgeLimit());
	}

	@Test
	@DisplayName("findTicket: Ticket 없을 경우 EntityNotFoundException 발생한다")
	void findTicketCase1() {
		// Given
		Long ticketId = 1L;

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> ticketService.findTicket(ticketId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("findTicket: Ticket 있을 경우 조회 후 반환한다")
	void findTicketCase2() {
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
		Ticket result = ticketService.findTicket(ticketId);

		// Then
		Assertions.assertThat(ticket.getProduct().getName()).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(ticket.getProduct().getDescription()).isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(ticket.getProduct().getPrice()).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(ticket.getProduct().getQuantity()).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(ticket.getPlace()).isEqualTo(result.getPlace());
		Assertions.assertThat(ticket.getPerformanceTime()).isEqualTo(result.getPerformanceTime());
		Assertions.assertThat(ticket.getDuration()).isEqualTo(result.getDuration());
		Assertions.assertThat(ticket.getAgeLimit()).isEqualTo(result.getAgeLimit());
	}

	@Test
	@DisplayName("updateTicket: Ticket 없을 경우 EntityNotFoundException 발생한다")
	void updateTicketCase1() {
		// Given
		Long ticketId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() ->
				ticketService.updateTicket(
					ticketId,
					name,
					description,
					price,
					quantity,
					place,
					performanceTime,
					duration,
					ageLimit
				)
			)
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateTicket: Ticket 있을 경우 수정 후 반환한다")
	void updateTicketCase2() {
		// Given
		Long ticketId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";

		Ticket ticket = Ticket.create(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

		// When
		Ticket result = ticketService.updateTicket(
			ticketId,
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		// Then
		Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(place).isEqualTo(result.getPlace());
		Assertions.assertThat(performanceTime).isEqualTo(result.getPerformanceTime());
		Assertions.assertThat(duration).isEqualTo(result.getDuration());
		Assertions.assertThat(ageLimit).isEqualTo(result.getAgeLimit());

		Mockito.verify(ticketRepository, Mockito.never()).save(Mockito.any());
	}

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
