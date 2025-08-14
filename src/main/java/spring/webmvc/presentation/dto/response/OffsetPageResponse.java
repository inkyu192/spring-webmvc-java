package spring.webmvc.presentation.dto.response;

import org.springframework.data.domain.Page;

public record OffsetPageResponse(
	int page,
	int size,
	long totalElements,
	int totalPages,
	boolean hasNext,
	boolean hasPrevious
) {
	public OffsetPageResponse(Page<?> page) {
		this(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.hasNext(),
			page.hasPrevious()
		);
	}
}
