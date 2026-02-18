package spring.webmvc.infrastructure.external.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
	PROFILE("profile"),
	BANNER("banner"),
	;

	private final String path;
}
