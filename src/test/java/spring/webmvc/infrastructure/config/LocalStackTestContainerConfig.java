package spring.webmvc.infrastructure.config;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class LocalStackTestContainerConfig {

	@Container
	public static final LocalStackContainer localStackContainer =
		new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.8"))
			.withServices(
				LocalStackContainer.Service.S3,
				LocalStackContainer.Service.DYNAMODB,
				LocalStackContainer.Service.SQS
			)
			.withEnv("DEFAULT_REGION", "ap-northeast-2");

	static {
		localStackContainer.start();
	}
}
