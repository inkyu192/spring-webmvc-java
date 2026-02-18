package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QUser.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.vo.Phone;

@Repository
public class UserQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public UserQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public Page<User> findAllWithOffsetPage(
		Pageable pageable,
		Phone phone,
		String name,
		Instant createdFrom,
		Instant createdTo
	) {
		Long total = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(
				eqPhone(phone),
				eqName(name),
				betweenCreatedAt(createdFrom, createdTo)
			)
			.fetchOne();

		if (total == null) {
			total = 0L;
		}

		List<User> content = jpaQueryFactory
			.selectFrom(user)
			.where(
				eqPhone(phone),
				eqName(name),
				betweenCreatedAt(createdFrom, createdTo)
			)
			.orderBy(user.id.desc())
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression eqPhone(Phone phone) {
		return phone != null ? user.phone.eq(phone) : null;
	}

	private BooleanExpression eqName(String name) {
		return name != null ? user.name.eq(name) : null;
	}

	private BooleanExpression betweenCreatedAt(Instant from, Instant to) {
		return user.createdAt.between(from, to);
	}
}
