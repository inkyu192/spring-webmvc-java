package spring.webmvc.infrastructure.persistence.dynamodb;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import spring.webmvc.domain.model.entity.UserCurationProduct;
import spring.webmvc.infrastructure.exception.FailedAwsException;

@Slf4j
@Repository
public class UserCurationProductDynamoDbRepository {

	private final DynamoDbTable<UserCurationProduct> table;

	public UserCurationProductDynamoDbRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
		this.table = dynamoDbEnhancedClient.table(
			UserCurationProduct.TABLE_NAME,
			TableSchema.fromBean(UserCurationProduct.class)
		);
	}

	public UserCurationProduct findByUserIdAndCurationId(Long userId, Long curationId) {
		String pk = "USER#" + userId;
		String sk = "CURATION#" + curationId;

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
}
