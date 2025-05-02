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

import spring.webmvc.application.dto.TicketDto;
import spring.webmvc.domain.cache.TicketCache;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@InjectMocks
	private TicketService ticketService;

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private TicketCache ticketCache;

	@Mock
	private JsonSupport jsonSupport;

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
		Assertions.assertThat(result.getProduct().getName()).isEqualTo(name);
		Assertions.assertThat(result.getProduct().getDescription()).isEqualTo(description);
		Assertions.assertThat(result.getProduct().getPrice()).isEqualTo(price);
		Assertions.assertThat(result.getProduct().getQuantity()).isEqualTo(quantity);
		Assertions.assertThat(result.getPlace()).isEqualTo(place);
		Assertions.assertThat(result.getPerformanceTime()).isEqualTo(performanceTime);
		Assertions.assertThat(result.getDuration()).isEqualTo(duration);
		Assertions.assertThat(result.getAgeLimit()).isEqualTo(ageLimit);
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
		TicketDto result = ticketService.findTicket(ticketId);

		// Then
		Assertions.assertThat(result.name()).isEqualTo(ticket.getProduct().getName());
		Assertions.assertThat(result.description()).isEqualTo(ticket.getProduct().getDescription());
		Assertions.assertThat(result.price()).isEqualTo(ticket.getProduct().getPrice());
		Assertions.assertThat(result.quantity()).isEqualTo(ticket.getProduct().getQuantity());
		Assertions.assertThat(result.place()).isEqualTo(ticket.getPlace());
		Assertions.assertThat(result.performanceTime()).isEqualTo(ticket.getPerformanceTime());
		Assertions.assertThat(result.duration()).isEqualTo(ticket.getDuration());
		Assertions.assertThat(result.ageLimit()).isEqualTo(ticket.getAgeLimit());
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
		).isInstanceOf(EntityNotFoundException.class);
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
		Assertions.assertThat(result.getProduct().getName()).isEqualTo(ticket.getProduct().getName());
		Assertions.assertThat(result.getProduct().getDescription()).isEqualTo(ticket.getProduct().getDescription());
		Assertions.assertThat(result.getProduct().getPrice()).isEqualTo(ticket.getProduct().getPrice());
		Assertions.assertThat(result.getProduct().getQuantity()).isEqualTo(ticket.getProduct().getQuantity());
		Assertions.assertThat(result.getPlace()).isEqualTo(ticket.getPlace());
		Assertions.assertThat(result.getPerformanceTime()).isEqualTo(ticket.getPerformanceTime());
		Assertions.assertThat(result.getDuration()).isEqualTo(ticket.getDuration());
		Assertions.assertThat(result.getAgeLimit()).isEqualTo(ticket.getAgeLimit());
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
