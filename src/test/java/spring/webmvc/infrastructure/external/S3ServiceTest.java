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
import spring.webmvc.infrastructure.common.FileType;
import spring.webmvc.infrastructure.config.LocalStackTestContainerConfig;

class S3ServiceTest {

	private static final String BUCKET = "my-bucket";
	private static S3Client s3Client;
	private static S3Service s3Service;

	@BeforeAll
	static void init() {
		s3Client = S3Client.builder()
			.endpointOverride(
				LocalStackTestContainerConfig.LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.S3))
			.region(Region.of(LocalStackTestContainerConfig.LOCAL_STACK_CONTAINER.getRegion()))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(
						LocalStackTestContainerConfig.LOCAL_STACK_CONTAINER.getAccessKey(),
						LocalStackTestContainerConfig.LOCAL_STACK_CONTAINER.getSecretKey()
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
		String filename = "file.txt";
		String content = "content";

		MockMultipartFile multipartFile = new MockMultipartFile(
			filename, filename, MediaType.TEXT_PLAIN_VALUE, content.getBytes(StandardCharsets.UTF_8)
		);

		// When
		String key = s3Service.putObject(FileType.TEMP, multipartFile);

		// Then
		ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(BUCKET)
				.key(key)
				.build()
		);

		String result = new String(response.readAllBytes(), StandardCharsets.UTF_8);

		Assertions.assertThat(result).isEqualTo(content);
	}
}
