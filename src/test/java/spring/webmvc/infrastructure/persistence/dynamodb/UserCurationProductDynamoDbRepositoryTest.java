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
import spring.webmvc.domain.model.entity.UserCurationProduct;
import spring.webmvc.infrastructure.config.LocalStackTestContainerConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserCurationProductDynamoDbRepositoryTest {

	private DynamoDbClient dynamoDbClient;
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;
	private UserCurationProductDynamoDbRepository repository;

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

		repository = new UserCurationProductDynamoDbRepository(dynamoDbEnhancedClient);
	}

	@BeforeEach
	void setUp() {
		dynamoDbClient.createTable(
			CreateTableRequest.builder()
				.tableName(UserCurationProduct.TABLE_NAME)
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
				.tableName(UserCurationProduct.TABLE_NAME)
				.build()
		);
	}

	@Test
	@DisplayName("userId와 curationId로 UserCurationProduct를 조회한다")
	void findByUserIdAndCurationId() {
		Long userId = 1L;
		Long curationId = 11L;
		List<Long> productIds = List.of(1L, 3L, 11L, 13L);
		String createdAt = "2025-03-01T10:30:00Z";
		String updatedAt = "2025-03-01T10:30:00Z";

		var table = dynamoDbEnhancedClient.table(
			UserCurationProduct.TABLE_NAME,
			TableSchema.fromBean(UserCurationProduct.class)
		);

		UserCurationProduct userCurationProduct = new UserCurationProduct();
		userCurationProduct.setPk("USER#" + userId);
		userCurationProduct.setSk("CURATION#" + curationId);
		userCurationProduct.setProductIds(productIds);
		userCurationProduct.setCreatedAt(createdAt);
		userCurationProduct.setUpdatedAt(updatedAt);

		table.putItem(userCurationProduct);

		UserCurationProduct result = repository.findByUserIdAndCurationId(userId, curationId);

		assertThat(result).isNotNull();
		assertThat(result.getPk()).isEqualTo("USER#" + userId);
		assertThat(result.getSk()).isEqualTo("CURATION#" + curationId);
		assertThat(result.getProductIds()).containsExactlyElementsOf(productIds);
		assertThat(result.getCreatedAt()).isEqualTo(createdAt);
		assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
	}

	@Test
	@DisplayName("존재하지 않는 데이터 조회 시 null을 반환한다")
	void findByUserIdAndCurationIdNotFound() {
		UserCurationProduct result = repository.findByUserIdAndCurationId(999L, 999L);

		assertThat(result).isNull();
	}
}
