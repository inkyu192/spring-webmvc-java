package spring.web.java.infrastructure.adapter.out;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.ItemJpaCustomRepositoryPort;
import spring.web.java.domain.Item;

@Repository
@RequiredArgsConstructor
public class ItemJpaCustomRepositoryAdapter implements ItemJpaCustomRepositoryPort {

	private final EntityManager entityManager;

	@Override
	public Page<Item> findAll(Pageable pageable, String name) {
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
}
