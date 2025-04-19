package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.TicketService;
import spring.webmvc.presentation.dto.request.TicketCreateRequest;
import spring.webmvc.presentation.dto.request.TicketUpdateRequest;
import spring.webmvc.presentation.dto.response.TicketResponse;

@RestController
@RequestMapping("/products/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public TicketResponse findTicket(@PathVariable Long id) {
		return ticketService.findTicket(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public TicketResponse createTicket(@RequestBody @Validated TicketCreateRequest ticketCreateRequest) {
		return ticketService.createTicket(ticketCreateRequest);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public TicketResponse updateTicket(
		@PathVariable Long id,
		@RequestBody @Validated TicketUpdateRequest ticketUpdateRequest
	) {
		return ticketService.updateTicket(id, ticketUpdateRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTicket(@PathVariable Long id) {
		ticketService.deleteTicket(id);
	}
}
