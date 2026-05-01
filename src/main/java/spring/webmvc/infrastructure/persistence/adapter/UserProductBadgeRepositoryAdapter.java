package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.repository.UserProductBadgeRepository;
import spring.webmvc.infrastructure.persistence.dynamodb.UserProductBadgeDynamoDbRepository;

@Component
@RequiredArgsConstructor
public class UserProductBadgeRepositoryAdapter implements UserProductBadgeRepository {

	private final UserProductBadgeDynamoDbRepository dynamoDbRepository;

	@Override
	public UserProductBadge findByUserIdAndProductId(Long userId, Long productId) {
		return dynamoDbRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public List<UserProductBadge> findByUserIdAndProductIds(Long userId, List<Long> productIds) {
		return dynamoDbRepository.findByUserIdAndProductIds(userId, productIds);
	}
}
