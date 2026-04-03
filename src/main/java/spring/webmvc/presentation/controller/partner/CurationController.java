package spring.webmvc.presentation.controller.partner;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.enums.CurationCategory;
import spring.webmvc.presentation.dto.request.CurationCreateRequest;
import spring.webmvc.presentation.dto.response.CurationDetailOffsetPageResponse;
import spring.webmvc.presentation.dto.response.CurationDetailResponse;
import spring.webmvc.presentation.dto.response.CurationListResponse;

@RestController("partnerCurationController")
@RequestMapping("/partner/curations")
@RequiredArgsConstructor
public class CurationController {

	private final CurationService curationService;

	@PostMapping
	@PreAuthorize("hasAuthority('CURATION_CREATE')")
	@ResponseStatus(HttpStatus.CREATED)
	public CurationDetailResponse createCuration(
		@RequestBody @Validated CurationCreateRequest request
	) {
		CurationCreateCommand command = request.toCommand();
		CurationDetailResult result = curationService.createCuration(command);

		return CurationDetailResponse.of(result);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CURATION_READ')")
	public CurationListResponse findCurations(
		@RequestParam CurationCategory category
	) {
		List<CurationSummaryResult> results = curationService.findCurations(category);

		return CurationListResponse.of(results);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CURATION_READ')")
	public CurationDetailOffsetPageResponse findCuration(
		@PathVariable Long id,
		@PageableDefault Pageable pageable
	) {
		CurationOffsetPageResult result = curationService.findCurationProductWithOffsetPage(id, pageable);

		return CurationDetailOffsetPageResponse.of(result);
	}
}
