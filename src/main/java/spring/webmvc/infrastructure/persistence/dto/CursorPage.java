package spring.webmvc.infrastructure.persistence.dto;

import java.util.List;
import java.util.function.Function;

public record CursorPage<T>(
	List<T> content,
	int size,
	boolean hasNext,
	Long nextCursorId
) {

	public CursorPage(List<T> content, int size, Function<T, Long> getCursorId) {
		this(
			content.size() > size ? content.subList(0, content.size() - 1) : content,
			size,
			content.size() > size,
			content.size() > size ? getCursorId.apply(content.getLast()) : null
		);
	}
}
