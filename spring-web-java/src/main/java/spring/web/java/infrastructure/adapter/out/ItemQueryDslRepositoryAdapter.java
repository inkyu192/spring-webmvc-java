package spring.web.java.infrastructure.adapter.out;

import static spring.web.java.domain.QItem.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.web.java.application.port.out.ItemQueryDslRepositoryPort;
import spring.web.java.domain.Item;

@Repository
public class ItemQueryDslRepositoryAdapter implements ItemQueryDslRepositoryPort {

	private final JPAQueryFactory queryFactory;

	public ItemQueryDslRepositoryAdapter(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Item> findAll(Pageable pageable, String name) {
		int count = queryFactory
			.selectOne()
			.from(item)
			.where(
				StringUtils.hasText(name) ? item.name.like("%" + name + "%") : null
			)
			.fetch()
			.size();

		List<Item> content = queryFactory
			.select(item)
			.from(item)
			.where(
				StringUtils.hasText(name) ? item.name.like("%" + name + "%") : null
			)
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}
}
