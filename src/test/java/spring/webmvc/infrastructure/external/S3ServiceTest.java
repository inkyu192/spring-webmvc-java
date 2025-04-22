package spring.webmvc.infrastructure.external;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import spring.webmvc.infrastructure.config.LocalStackTestConfig;

class S3ServiceTest {

	private static final String BUCKET = "my-bucket";
	private static S3Client s3Client;
	private static S3Service s3Service;

	@BeforeAll
	static void init() {
		s3Client = S3Client.builder()
			.endpointOverride(
				LocalStackTestConfig.LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.S3))
			.region(Region.of(LocalStackTestConfig.LOCAL_STACK_CONTAINER.getRegion()))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(
						LocalStackTestConfig.LOCAL_STACK_CONTAINER.getAccessKey(),
						LocalStackTestConfig.LOCAL_STACK_CONTAINER.getSecretKey()
					)
				)
			)
			.build();

		s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET).build());

		s3Service = new S3Service(s3Client);
	}

	@Test
	@DisplayName("putObject: MultipartFile S3 업로드 후 key 반환한다")
	void putObject() throws Exception {
		// Given
		String directory = "directory";
		String filename = "file.txt";
		String content = "content";

		MockMultipartFile multipartFile = new MockMultipartFile(
			filename, filename, MediaType.TEXT_PLAIN_VALUE, content.getBytes(StandardCharsets.UTF_8)
		);

		// When
		String key = s3Service.putObject(BUCKET, directory, multipartFile);

		// Then
		ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(BUCKET)
				.key(key)
				.build()
		);

		String downloaded = new String(response.readAllBytes(), StandardCharsets.UTF_8);
		Assertions.assertThat(downloaded).isEqualTo(content);
	}
}
