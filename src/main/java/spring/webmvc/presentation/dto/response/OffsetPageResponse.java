package spring.webmvc.presentation.dto.response;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record OffsetPageResponse<T>(
	int page,
	int size,
	long totalElements,
	int totalPages,
	boolean hasNext,
	boolean hasPrevious,
	List<T> content
) {
	public static <T, R> OffsetPageResponse<R> of(
		Page<T> page,
		Function<T, R> mapper
	) {
		return new OffsetPageResponse<>(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.hasNext(),
			page.hasPrevious(),
			page.getContent().stream()
				.map(mapper)
				.toList()
		);
	}
}
