package spring.webmvc.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestConfiguration
public class LocalStackTestContainerConfig {

	@Container
	public static final LocalStackContainer LOCAL_STACK_CONTAINER =
		new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
			.withServices(
				LocalStackContainer.Service.S3
			)
			.withEnv("DEFAULT_REGION", "ap-northeast-2");

	static {
		LOCAL_STACK_CONTAINER.start();
	}
}
