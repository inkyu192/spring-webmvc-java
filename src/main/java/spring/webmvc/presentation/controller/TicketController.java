package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.TicketService;
import spring.webmvc.presentation.dto.request.TicketUpdateRequest;
import spring.webmvc.presentation.dto.response.TicketResponse;

@RestController
@RequestMapping("/products/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public TicketResponse updateTicket(
		@PathVariable Long id,
		@RequestBody @Validated TicketUpdateRequest ticketUpdateRequest
	) {
		return new TicketResponse(
			ticketService.updateTicket(
				id,
				ticketUpdateRequest.name(),
				ticketUpdateRequest.description(),
				ticketUpdateRequest.price(),
				ticketUpdateRequest.quantity(),
				ticketUpdateRequest.place(),
				ticketUpdateRequest.performanceTime(),
				ticketUpdateRequest.duration(),
				ticketUpdateRequest.ageLimit()
			)
		);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTicket(@PathVariable Long id) {
		ticketService.deleteTicket(id);
	}
}
