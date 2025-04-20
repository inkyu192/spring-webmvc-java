package spring.webmvc.infrastructure.external;

import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import spring.webmvc.presentation.exception.AwsIntegrationException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	public String putObject(String bucket, String directory, MultipartFile file) {
		String filename = file.getOriginalFilename();
		String key = generateKey(directory, filename);
		String contentType = URLConnection.guessContentTypeFromName(filename);

		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.contentType(contentType)
			.build();

		try {
			s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (S3Exception | IOException e) {
			log.error("Failed to put object to S3", e);
			throw new AwsIntegrationException("S3", e);
		}

		return key;
	}

	private String generateKey(String directory, String filename) {
		String extension = extractExtension(filename);
		String localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uuid = UUID.randomUUID().toString();

		return String.format("%s/%s/%s.%s", directory, localDate, uuid, extension);
	}

	private String extractExtension(String filename) {
		int lastDot = filename.lastIndexOf('.');
		if (lastDot == -1 || lastDot == filename.length() - 1) {
			return "bin";
		}
		return filename.substring(lastDot + 1);
	}
}
