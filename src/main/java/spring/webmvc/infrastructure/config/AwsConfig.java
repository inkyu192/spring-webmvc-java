package spring.webmvc.infrastructure.config;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AwsConfig {

	private final Environment environment;

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		if (isLocal()) {
			return StaticCredentialsProvider.create(
				AwsBasicCredentials.create("accessKey", "secretKey")
			);
		}
		return DefaultCredentialsProvider.create();
	}

	@Bean
	public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
		S3ClientBuilder builder = S3Client.builder()
			.region(Region.AP_NORTHEAST_2)
			.credentialsProvider(awsCredentialsProvider);

		if (isLocal()) {
			builder
				.endpointOverride(URI.create("http://localhost:4566"))
				.serviceConfiguration(
					S3Configuration.builder()
						.pathStyleAccessEnabled(true)
						.build()
				);
		}

		return builder.build();
	}

	private boolean isLocal() {
		return Arrays.asList(environment.getActiveProfiles()).contains("local");
	}
}
