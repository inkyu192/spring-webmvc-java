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
import spring.webmvc.application.service.AccommodationService;
import spring.webmvc.presentation.dto.request.AccommodationSaveRequest;
import spring.webmvc.presentation.dto.response.AccommodationResponse;

@RestController
@RequestMapping("/products/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

	private final AccommodationService accommodationService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public AccommodationResponse findAccommodation(@PathVariable Long id) {
		return accommodationService.findAccommodation(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public AccommodationResponse createAccommodation(
		@RequestBody @Validated AccommodationSaveRequest accommodationSaveRequest) {
		return accommodationService.createAccommodation(accommodationSaveRequest);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public AccommodationResponse patchAccommodation(
		@PathVariable Long id,
		@RequestBody @Validated AccommodationSaveRequest accommodationSaveRequest
	) {
		return accommodationService.updateAccommodation(id, accommodationSaveRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAccommodation(@PathVariable Long id) {
		accommodationService.deleteAccommodation(id);
	}
}
