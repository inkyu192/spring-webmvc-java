package spring.webmvc.presentation.dto.response;

import spring.webmvc.infrastructure.common.FileType;

public record FileRequest(
	FileType type
) {
}
