package spring.webmvc.domain.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record CursorPage<T>(
	List<T> content,
	Long size,
	Boolean hasNext,
	Long nextCursorId
) {
	public static <T> CursorPage<T> create(List<T> content, Long size, Function<T, Long> getCursorId) {
		List<T> actualContent;
		Long cursorId;
		boolean hasNextPage = content.size() > size;

		if (hasNextPage) {
			actualContent = content.subList(0, content.size() - 1);
			cursorId = getCursorId.apply(content.getLast());
		} else {
			actualContent = content;
			cursorId = null;
		}

		return new CursorPage<>(actualContent, size, hasNextPage, cursorId);
	}

	public <U> CursorPage<U> map(Function<T, U> transform) {
		List<U> mappedContent = content.stream()
			.map(transform)
			.collect(Collectors.toList());

		return new CursorPage<>(mappedContent, size, hasNext, nextCursorId);
	}
}
