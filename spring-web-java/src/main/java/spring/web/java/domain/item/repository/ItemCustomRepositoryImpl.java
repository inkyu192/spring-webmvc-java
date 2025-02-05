package spring.web.java.domain.item.repository;

import static spring.web.java.domain.item.QItem.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import spring.web.java.domain.item.Item;

public class ItemCustomRepositoryImpl implements ItemCustomRepository {

	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;

	public ItemCustomRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Item> findAllUsingJpql(Pageable pageable, String name) {
		String countJpql = """
			SELECT COUNT(i)
			FROM Item i
			WHERE 1 = 1
			""";
		String contentJpql = """
			SELECT i
			FROM Item i
			WHERE 1 = 1
			""";

		if (StringUtils.hasText(name)) {
			countJpql += " AND i.name LIKE CONCAT('%', :name, '%')";
			contentJpql += " AND i.name LIKE CONCAT('%', :name, '%')";
		}

		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			List<String> orderList = sort
				.map(order -> " i." + order.getProperty() + " " + order.getDirection())
				.toList();

			contentJpql += " ORDER BY " + String.join(",", orderList);
		}

		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		TypedQuery<Item> contentQuery = entityManager.createQuery(contentJpql, Item.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize());

		if (StringUtils.hasText(name)) {
			countQuery.setParameter("name", name);
			contentQuery.setParameter("name", name);
		}

		return new PageImpl<>(contentQuery.getResultList(), pageable, countQuery.getSingleResult());
	}

	@Override
	public Page<Item> findAllUsingQueryDsl(Pageable pageable, String name) {
		int count = queryFactory
			.selectOne()
			.from(item)
			.where(likeName(name))
			.fetch()
			.size();

		List<Item> content = queryFactory
			.select(item)
			.from(item)
			.where(likeName(name))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression likeName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return item.name.like("%" + name + "%");
	}
}
