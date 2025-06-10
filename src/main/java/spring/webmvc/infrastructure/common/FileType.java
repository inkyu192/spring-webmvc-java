package spring.webmvc.infrastructure.common;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
	PROFILE("profile", List.of("jpg", "png"), 10 * 1024 * 1024),
	BANNER("banner", List.of("jpg", "png"), 10 * 1024 * 1024),
	TEMP("temp", List.of("jpg", "png", "pdf"), 10 * 1024 * 1024);

	private final String directory;
	private final List<String> allowedExtensions;
	private final long maxSize;
}
