package spring.webmvc.infrastructure.config;

import java.time.Duration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Testcontainers
@TestConfiguration
public class MongoTestContainerConfig {

	private static final int MONGO_PORT = 27017;

	@Container
	private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
		.withExposedPorts(MONGO_PORT)
		.waitingFor(Wait.forListeningPort())
		.withStartupTimeout(Duration.ofSeconds(60));

	static {
		mongoDBContainer.start();
	}

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, "test-db");
	}
}
