package spring.webmvc.infrastructure.persistence.dynamodb;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import spring.webmvc.domain.model.entity.UserCurationProduct;

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

		return table.getItem(key);
	}

	public void save(UserCurationProduct userCurationProduct) {
		table.putItem(userCurationProduct);
	}

	public void delete(Long userId, Long curationId) {
		String pk = "USER#" + userId;
		String sk = "CURATION#" + curationId;

		Key key = Key.builder()
			.partitionValue(pk)
			.sortValue(sk)
			.build();

		table.deleteItem(key);
	}
}
