package spring.webmvc.presentation.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.webmvc.application.command.CurationCreateCommand;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.presentation.dto.request.CurationCreateRequest;
import spring.webmvc.presentation.dto.response.CurationResponse;
import spring.webmvc.presentation.dto.response.ProductResponse;

@RestController
@RequestMapping("/curations")
@RequiredArgsConstructor
public class CurationController {

	private final CurationService curationService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public CurationResponse createCuration(@Valid @RequestBody CurationCreateRequest request) {
		CurationCreateCommand command = new CurationCreateCommand(request);
		return new CurationResponse(curationService.createCuration(command));
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<CurationResponse> findCurations() {
		return curationService.findCurations().stream()
			.map(CurationResponse::new)
			.toList();
	}

	@GetMapping("/{id}")
	public Page<ProductResponse> findCuration(@PageableDefault Pageable pageable, @PathVariable Long id) {
		return curationService.findCurationProduct(pageable, id)
			.map(ProductResponse::new);
	}
}
