package spring.webmvc.presentation.dto.request;

import spring.webmvc.infrastructure.common.FileType;

public record FileUploadRequest(
	FileType type
) {
}
