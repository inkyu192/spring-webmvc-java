package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QUser.*;
import static spring.webmvc.domain.model.entity.QUserCredential.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.UserCredential;

@Repository
public class UserCredentialQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public UserCredentialQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public Optional<UserCredential> findByUserId(Long userId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.selectFrom(userCredential)
				.join(userCredential.user, user).fetchJoin()
				.where(eqUserId(userId))
				.fetchOne()
		);
	}

	private BooleanExpression eqUserId(Long userId) {
		return userCredential.user.id.eq(userId);
	}
}
