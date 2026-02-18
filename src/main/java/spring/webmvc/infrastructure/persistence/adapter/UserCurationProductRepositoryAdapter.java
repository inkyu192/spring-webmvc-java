package spring.webmvc.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.UserCurationProduct;
import spring.webmvc.domain.repository.UserCurationProductRepository;
import spring.webmvc.infrastructure.persistence.dynamodb.UserCurationProductDynamoDbRepository;

@Component
@RequiredArgsConstructor
public class UserCurationProductRepositoryAdapter implements UserCurationProductRepository {

	private final UserCurationProductDynamoDbRepository dynamoDbRepository;

	@Override
	public UserCurationProduct findByUserIdAndCurationId(Long userId, Long curationId) {
		return dynamoDbRepository.findByUserIdAndCurationId(userId, curationId);
	}
}
