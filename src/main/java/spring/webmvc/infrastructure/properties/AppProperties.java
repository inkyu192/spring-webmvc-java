package spring.webmvc.infrastructure.properties;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
	String baseUrl,
	String docsUrl,
	JwtProperties jwt,
	CryptoProperties crypto,
	CorsProperties cors,
	AwsProperties aws,
	ExternalProperties external
) {
	public record JwtProperties(
		TokenProperties accessToken,
		TokenProperties refreshToken
	) {
		public record TokenProperties(
			String key,
			Duration expiration
		) {
		}
	}

	public record CryptoProperties(
		String secretKey,
		String ivParameter
	) {
	}

	public record CorsProperties(
		List<String> allowedOrigins,
		List<String> allowedOriginPatterns,
		List<String> allowedMethods,
		List<String> allowedHeaders
	) {
	}

	public record AwsProperties(
		S3Properties s3,
		DynamoDbProperties dynamodb,
		CloudFrontProperties cloudfront
	) {
		public record S3Properties(
			String endpoint,
			String bucket
		) {
		}

		public record DynamoDbProperties(
			String endpoint
		) {
		}

		public record CloudFrontProperties(
			String domain
		) {
		}
	}

	public record ExternalProperties(
		NotificationProperties notification
	) {
		public record NotificationProperties(
			String host
		) {
		}
	}
}
