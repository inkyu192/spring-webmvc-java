package spring.webmvc.infrastructure.external;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import spring.webmvc.infrastructure.config.LocalStackTestContainerConfig;
import spring.webmvc.infrastructure.external.s3.FileType;
import spring.webmvc.infrastructure.external.s3.S3Service;
import spring.webmvc.infrastructure.properties.AppProperties;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class S3ServiceTest {

	private S3Client s3Client;
	private S3Service s3Service;
	private final String bucket = "test-bucket";

	@BeforeAll
	void setUpAll() {
		s3Client = S3Client.builder()
			.endpointOverride(
				LocalStackTestContainerConfig.localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
			.region(Region.of(LocalStackTestContainerConfig.localStackContainer.getRegion()))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(
						LocalStackTestContainerConfig.localStackContainer.getAccessKey(),
						LocalStackTestContainerConfig.localStackContainer.getSecretKey()
					)
				)
			)
			.forcePathStyle(true)
			.build();

		s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());

		AppProperties appProperties = mock(AppProperties.class);
		AppProperties.AwsProperties awsProperties = mock(AppProperties.AwsProperties.class);
		AppProperties.AwsProperties.S3Properties s3Properties = mock(AppProperties.AwsProperties.S3Properties.class);

		when(appProperties.aws()).thenReturn(awsProperties);
		when(awsProperties.s3()).thenReturn(s3Properties);
		when(s3Properties.bucket()).thenReturn(bucket);

		s3Service = new S3Service(s3Client, appProperties);
	}

	@AfterEach
	void tearDown() {
		var listResponse = s3Client.listObjectsV2(
			ListObjectsV2Request.builder()
				.bucket(bucket)
				.build()
		);

		listResponse.contents().forEach(obj -> {
			s3Client.deleteObject(
				DeleteObjectRequest.builder()
					.bucket(bucket)
					.key(obj.key())
					.build()
			);
		});
	}

	@Test
	@DisplayName("MultipartFile S3 업로드 후 key 반환한다")
	void putObject() throws Exception {
		String filename = "file.jpg";
		String content = "content";

		MockMultipartFile file = new MockMultipartFile(
			filename,
			filename,
			MediaType.IMAGE_JPEG_VALUE,
			content.getBytes()
		);

		String key = s3Service.putObject(file);

		var response = s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build()
		);

		String result = new String(response.readAllBytes());

		assertThat(result).isEqualTo(content);
	}

	@Test
	@DisplayName("S3 객체를 복사한다")
	void copyObject() throws Exception {
		String filename = "file.jpg";
		String content = "content";

		MockMultipartFile file = new MockMultipartFile(
			filename,
			filename,
			MediaType.IMAGE_JPEG_VALUE,
			content.getBytes()
		);

		String sourceKey = s3Service.putObject(file);
		Long id = 123L;

		String destinationKey = s3Service.copyObject(sourceKey, FileType.PROFILE, id);

		String fileName = sourceKey.substring(sourceKey.lastIndexOf('/') + 1);
		String expectedDestinationKey = "data/" + FileType.PROFILE.getPath() + "/" + id + "/" + fileName;
		assertThat(destinationKey).isEqualTo(expectedDestinationKey);

		var response = s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucket)
				.key(destinationKey)
				.build()
		);

		assertThat(new String(response.readAllBytes())).isEqualTo(content);
	}
}
