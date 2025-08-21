package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.presentation.dto.request.CurationCreateRequest;
import spring.webmvc.presentation.dto.response.CurationCreateResponse;
import spring.webmvc.presentation.dto.response.CurationListResponse;
import spring.webmvc.presentation.dto.response.CurationProductResponse;

@RestController
@RequestMapping("/curations")
@RequiredArgsConstructor
public class CurationController {

	private final CurationService curationService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public CurationCreateResponse createCuration(@Valid @RequestBody CurationCreateRequest request) {
		return new CurationCreateResponse(curationService.createCuration(request.toCommand()));
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public CurationListResponse findCurations() {
		return new CurationListResponse(curationService.findCurations());
	}

	@GetMapping("/{id}")
	public CurationProductResponse findCuration(
		@PathVariable Long id,
		@RequestParam(required = false, defaultValue = "10") Integer size,
		@RequestParam(required = false) Long cursorId
	) {
		return new CurationProductResponse(curationService.findCurationProduct(id, cursorId, size));
	}
}
