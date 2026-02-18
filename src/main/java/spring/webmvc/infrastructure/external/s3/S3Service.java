package spring.webmvc.infrastructure.external.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import spring.webmvc.infrastructure.exception.FailedAwsIntegrationException;
import spring.webmvc.infrastructure.properties.AppProperties;

@Component
@Slf4j
public class S3Service {
	private final S3Client s3Client;
	private final String bucket;

	public S3Service(S3Client s3Client, AppProperties appProperties) {
		this.s3Client = s3Client;
		this.bucket = appProperties.aws().s3().bucket();
	}

	public String putObject(MultipartFile file) {
		String filename = file.getOriginalFilename();

		if (filename == null) {
			throw new IllegalArgumentException();
		}

		String key = "temp/%s.%s".formatted(UUID.randomUUID(), extractExtension(filename));

		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.contentType(file.getContentType())
			.build();

		try {
			s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (IOException e) {
			log.error("Failed to put object to S3", e);
			throw new FailedAwsIntegrationException("S3", e);
		}

		return key;
	}

	public String copyObject(
		String sourceKey,
		FileType fileType,
		Long id
	) {
		String fileName = sourceKey.substring(sourceKey.lastIndexOf('/') + 1);
		String destinationKey = "data/%s/%d/%s".formatted(fileType.getPath(), id, fileName);

		CopyObjectRequest copyRequest = CopyObjectRequest.builder()
			.sourceBucket(bucket)
			.sourceKey(sourceKey)
			.destinationBucket(bucket)
			.destinationKey(destinationKey)
			.build();

		try {
			s3Client.copyObject(copyRequest);
		} catch (Exception e) {
			log.error("Failed to copy object to S3", e);
			throw new FailedAwsIntegrationException("S3", e);
		}

		return destinationKey;
	}

	private String extractExtension(String filename) {
		int lastDot = filename.lastIndexOf('.');

		if (lastDot == -1 || lastDot == filename.length() - 1) {
			return "bin";
		} else {
			return filename.substring(lastDot + 1);
		}
	}
}
