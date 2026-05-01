package spring.webmvc.infrastructure.persistence.dynamodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.infrastructure.exception.FailedAwsException;

@Slf4j
@Repository
public class UserProductBadgeDynamoDbRepository {

	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
	private final DynamoDbTable<UserProductBadge> table;

	public UserProductBadgeDynamoDbRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
		this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
		this.table = dynamoDbEnhancedClient.table(
			UserProductBadge.TABLE_NAME,
			TableSchema.fromBean(UserProductBadge.class)
		);
	}

	public UserProductBadge findByUserIdAndProductId(Long userId, Long productId) {
		String pk = "USER#" + userId;
		String sk = "PRODUCT#" + productId;

		Key key = Key.builder()
			.partitionValue(pk)
			.sortValue(sk)
			.build();

		try {
			return table.getItem(key);
		} catch (DynamoDbException e) {
			log.error("Failed to get item from DynamoDB", e);
			throw new FailedAwsException(e.awsErrorDetails().serviceName(), e);
		}
	}

	public List<UserProductBadge> findByUserIdAndProductIds(Long userId, List<Long> productIds) {
		if (productIds == null || productIds.isEmpty()) {
			return List.of();
		}

		ReadBatch.Builder<UserProductBadge> batchBuilder = ReadBatch.builder(UserProductBadge.class)
			.mappedTableResource(table);

		for (Long productId : productIds) {
			String pk = "USER#" + userId;
			String sk = "PRODUCT#" + productId;
			batchBuilder.addGetItem(Key.builder().partitionValue(pk).sortValue(sk).build());
		}

		try {
			BatchGetResultPageIterable result = dynamoDbEnhancedClient.batchGetItem(
				BatchGetItemEnhancedRequest.builder()
					.readBatches(batchBuilder.build())
					.build()
			);

			List<UserProductBadge> badges = new ArrayList<>();
			result.resultsForTable(table).forEach(badges::add);
			return badges;
		} catch (DynamoDbException e) {
			log.error("Failed to batch get items from DynamoDB", e);
			throw new FailedAwsException(e.awsErrorDetails().serviceName(), e);
		}
	}
}
