package spring.webmvc.presentation.dto.response;

import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public record CursorPageResponse(
	int size,
	Boolean hasNext,
	Long nextCursorId
) {
	public CursorPageResponse(CursorPage<?> page) {
		this(
			page.size(),
			page.hasNext(),
			page.nextCursorId()
		);
	}
}
