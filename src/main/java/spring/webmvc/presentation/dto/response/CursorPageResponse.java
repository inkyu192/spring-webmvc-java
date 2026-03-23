package spring.webmvc.presentation.dto.response;

import java.util.List;
import java.util.function.Function;

import spring.webmvc.domain.dto.CursorPage;

public record CursorPageResponse<T>(
	Long size,
	boolean hasNext,
	Long nextCursorId,
	List<T> content
) {
	public static <T, R> CursorPageResponse<R> of(
		CursorPage<T> page,
		Function<T, R> mapper
	) {
		return new CursorPageResponse<>(
			page.size(),
			page.hasNext(),
			page.nextCursorId(),
			page.content().stream()
				.map(mapper)
				.toList()
		);
	}
}
