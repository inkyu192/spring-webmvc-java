package spring.webmvc.presentation.controller.customer;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.enums.CurationCategory;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.dto.response.CurationDetailCursorPageResponse;
import spring.webmvc.presentation.dto.response.CurationListResponse;

@RestController("customerCurationController")
@RequestMapping("/customer/curations")
@RequiredArgsConstructor
public class CurationController {

	private final CurationService curationService;

	@GetMapping
	public CurationListResponse findCurations(
		@RequestParam(required = false) CurationCategory category
	) {
		List<CurationSummaryResult> results = curationService.findCurationsCached(category);

		return CurationListResponse.of(results);
	}

	@GetMapping("/{id}")
	public CurationDetailCursorPageResponse findCuration(
		@PathVariable Long id,
		@RequestParam(required = false) Long cursorId
	) {
		CurationCursorPageResult result = curationService.findCurationProductCached(
			SecurityContextUtil.getUserIdOrNull(),
			id,
			cursorId
		);

		return CurationDetailCursorPageResponse.of(result);
	}
}
