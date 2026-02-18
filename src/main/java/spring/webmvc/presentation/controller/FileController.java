package spring.webmvc.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.external.s3.S3Service;
import spring.webmvc.infrastructure.properties.AppProperties;
import spring.webmvc.presentation.dto.response.FileResponse;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

	private final S3Service s3Service;
	private final AppProperties appProperties;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public FileResponse uploadFile(
		@RequestPart MultipartFile file
	) {
		String key = s3Service.putObject(file);

		return FileResponse.of(key, appProperties.aws().cloudfront().domain());
	}
}
