package spring.webmvc.infrastructure.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.ses.SesClient;
import spring.webmvc.infrastructure.properties.AppProperties;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AwsConfig {

	private final AppProperties appProperties;

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		return StaticCredentialsProvider.create(
			AwsBasicCredentials.create("accessKey", "secretKey")
		);
	}

	@Bean
	public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
		return S3Client.builder()
			.region(Region.AP_NORTHEAST_2)
			.credentialsProvider(awsCredentialsProvider)
			.endpointOverride(URI.create(appProperties.aws().s3().endpoint()))
			.serviceConfiguration(
				S3Configuration.builder()
					.pathStyleAccessEnabled(true)
					.build()
			)
			.build();
	}

	@Bean
	public DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider) {
		return DynamoDbClient.builder()
			.region(Region.AP_NORTHEAST_2)
			.credentialsProvider(awsCredentialsProvider)
			.endpointOverride(URI.create(appProperties.aws().dynamodb().endpoint()))
			.build();
	}

	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder()
			.dynamoDbClient(dynamoDbClient)
			.build();
	}

	@Bean
	public SesClient sesClient(AwsCredentialsProvider awsCredentialsProvider) {
		return SesClient.builder()
			.region(Region.AP_NORTHEAST_2)
			.credentialsProvider(awsCredentialsProvider)
			.endpointOverride(URI.create(appProperties.aws().ses().endpoint()))
			.build();
	}
}
