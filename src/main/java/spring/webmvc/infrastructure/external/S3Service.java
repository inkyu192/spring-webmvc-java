package spring.webmvc.infrastructure.external;

import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import spring.webmvc.infrastructure.common.FileType;
import spring.webmvc.infrastructure.common.FileUtil;
import spring.webmvc.presentation.exception.AwsIntegrationException;

@Slf4j
@Component
public class S3Service {

	private final S3Client s3Client;
	private final String bucket;

	public S3Service(S3Client s3Client, @Value("${aws.s3.bucket}") String bucket) {
		this.s3Client = s3Client;
		this.bucket = bucket;
	}

	public String putObject(FileType fileType, MultipartFile file) {
		String filename = file.getOriginalFilename();
		String key = generateKey(filename, fileType.getDirectory());
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

	public void copyObject(String sourceKey, FileType destinationType) {
		String destinationKey = replaceDirectory(sourceKey, destinationType.getDirectory());

		CopyObjectRequest copyRequest = CopyObjectRequest.builder()
			.sourceBucket(bucket)
			.sourceKey(sourceKey)
			.destinationBucket(bucket)
			.destinationKey(destinationKey)
			.build();

		try {
			s3Client.copyObject(copyRequest);
		} catch (S3Exception e) {
			log.error("Failed to copy object to S3", e);
			throw new AwsIntegrationException("S3", e);
		}
	}

	private String generateKey(String filename, String directory) {
		String extension = FileUtil.extractExtension(filename);
		String localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uuid = UUID.randomUUID().toString();

		return String.format("%s/%s/%s.%s", directory, localDate, uuid, extension);
	}

	private String replaceDirectory(String sourceKey, String destinationDirectory) {
		String[] parts = sourceKey.split("/", 2);
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid sourceKey format: " + sourceKey);
		}
		return destinationDirectory + "/" + parts[1];
	}
}
