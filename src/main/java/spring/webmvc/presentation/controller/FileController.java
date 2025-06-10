package spring.webmvc.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.common.FileType;
import spring.webmvc.infrastructure.common.FileUtil;
import spring.webmvc.infrastructure.external.S3Service;
import spring.webmvc.presentation.dto.response.FileResponse;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

	private final S3Service s3Service;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public FileResponse uploadFile(@RequestParam MultipartFile file, @RequestParam FileType type) {
		FileUtil.validate(type, file);

		String key = s3Service.putObject("my-bucket", type.getDirectory(), file);
		return new FileResponse(key);
	}
}
