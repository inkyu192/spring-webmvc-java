package spring.webmvc.infrastructure.persistence.dynamodb;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.localstack.LocalStackContainer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.infrastructure.config.LocalStackTestContainerConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserProductBadgeDynamoDbRepositoryTest {

	private DynamoDbClient dynamoDbClient;
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;
	private UserProductBadgeDynamoDbRepository repository;

	@BeforeAll
	void setUpAll() {
		dynamoDbClient = DynamoDbClient.builder()
			.endpointOverride(LocalStackTestContainerConfig.localStackContainer.getEndpointOverride(
				LocalStackContainer.Service.DYNAMODB))
			.region(Region.of(LocalStackTestContainerConfig.localStackContainer.getRegion()))
			.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(
						LocalStackTestContainerConfig.localStackContainer.getAccessKey(),
						LocalStackTestContainerConfig.localStackContainer.getSecretKey()
					)
				)
			)
			.build();

		dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
			.dynamoDbClient(dynamoDbClient)
			.build();

		repository = new UserProductBadgeDynamoDbRepository(dynamoDbEnhancedClient);
	}

	@BeforeEach
	void setUp() {
		dynamoDbClient.createTable(
			CreateTableRequest.builder()
				.tableName(UserProductBadge.TABLE_NAME)
				.attributeDefinitions(
					AttributeDefinition.builder()
						.attributeName("PK")
						.attributeType(ScalarAttributeType.S)
						.build(),
					AttributeDefinition.builder()
						.attributeName("SK")
						.attributeType(ScalarAttributeType.S)
						.build()
				)
				.keySchema(
					KeySchemaElement.builder()
						.attributeName("PK")
						.keyType(KeyType.HASH)
						.build(),
					KeySchemaElement.builder()
						.attributeName("SK")
						.keyType(KeyType.RANGE)
						.build()
				)
				.billingMode(BillingMode.PAY_PER_REQUEST)
				.build()
		);
	}

	@AfterEach
	void tearDown() {
		dynamoDbClient.deleteTable(
			DeleteTableRequest.builder()
				.tableName(UserProductBadge.TABLE_NAME)
				.build()
		);
	}

	@Test
	@DisplayName("userId와 productId로 UserProductBadge를 조회한다")
	void findByUserIdAndProductId() {
		Long userId = 1L;
		Long productId = 1L;
		String createdAt = "2025-03-01T10:30:00Z";
		String updatedAt = "2025-03-01T10:30:00Z";

		var table = dynamoDbEnhancedClient.table(
			UserProductBadge.TABLE_NAME,
			TableSchema.fromBean(UserProductBadge.class)
		);

		UserProductBadge badge = new UserProductBadge();
		badge.setPk("USER#" + userId);
		badge.setSk("PRODUCT#" + productId);
		badge.setIsRecommended(true);
		badge.setIsPersonalPick(false);
		badge.setCreatedAt(createdAt);
		badge.setUpdatedAt(updatedAt);

		table.putItem(badge);

		UserProductBadge result = repository.findByUserIdAndProductId(userId, productId);

		assertThat(result).isNotNull();
		assertThat(result.getPk()).isEqualTo("USER#" + userId);
		assertThat(result.getSk()).isEqualTo("PRODUCT#" + productId);
		assertThat(result.getIsRecommended()).isTrue();
		assertThat(result.getIsPersonalPick()).isFalse();
		assertThat(result.getCreatedAt()).isEqualTo(createdAt);
		assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
	}

	@Test
	@DisplayName("존재하지 않는 데이터 조회 시 null을 반환한다")
	void findByUserIdAndProductIdNotFound() {
		UserProductBadge result = repository.findByUserIdAndProductId(999L, 999L);

		assertThat(result).isNull();
	}

	@Test
	@DisplayName("userId와 여러 productId로 UserProductBadge를 일괄 조회한다")
	void findByUserIdAndProductIds() {
		Long userId = 1L;
		String createdAt = "2025-03-01T10:30:00Z";
		String updatedAt = "2025-03-01T10:30:00Z";

		var table = dynamoDbEnhancedClient.table(
			UserProductBadge.TABLE_NAME,
			TableSchema.fromBean(UserProductBadge.class)
		);

		UserProductBadge badge1 = new UserProductBadge();
		badge1.setPk("USER#" + userId);
		badge1.setSk("PRODUCT#1");
		badge1.setIsRecommended(true);
		badge1.setIsPersonalPick(false);
		badge1.setCreatedAt(createdAt);
		badge1.setUpdatedAt(updatedAt);
		table.putItem(badge1);

		UserProductBadge badge2 = new UserProductBadge();
		badge2.setPk("USER#" + userId);
		badge2.setSk("PRODUCT#3");
		badge2.setIsRecommended(false);
		badge2.setIsPersonalPick(true);
		badge2.setCreatedAt(createdAt);
		badge2.setUpdatedAt(updatedAt);
		table.putItem(badge2);

		List<UserProductBadge> results = repository.findByUserIdAndProductIds(userId, List.of(1L, 3L, 999L));

		assertThat(results).hasSize(2);
	}

	@Test
	@DisplayName("빈 productIds로 조회 시 빈 리스트를 반환한다")
	void findByUserIdAndProductIdsEmpty() {
		List<UserProductBadge> results = repository.findByUserIdAndProductIds(1L, List.of());

		assertThat(results).isEmpty();
	}
}
