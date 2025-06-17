package spring.webmvc.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.common.FileUtil;
import spring.webmvc.infrastructure.external.S3Service;
import spring.webmvc.presentation.dto.request.FileUploadRequest;
import spring.webmvc.presentation.dto.response.FileResponse;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

	private final S3Service s3Service;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public FileResponse uploadFile(@RequestPart MultipartFile file, @RequestPart FileUploadRequest data) {
		FileUtil.validate(data.type(), file);

		String key = s3Service.putObject(data.type(), file);
		return new FileResponse(key);
	}
}
