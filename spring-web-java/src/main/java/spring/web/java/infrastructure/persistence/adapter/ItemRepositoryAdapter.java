package spring.web.java.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Item;
import spring.web.java.domain.repository.ItemRepository;
import spring.web.java.infrastructure.persistence.ItemJpaRepository;
import spring.web.java.infrastructure.persistence.ItemQuerydslRepository;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepository {

	private final ItemJpaRepository jpaRepository;
	private final ItemQuerydslRepository querydslRepository;

	@Override
	public Page<Item> findAll(Pageable pageable, String name) {
		return querydslRepository.findAll(pageable, name);
	}

	@Override
	public Optional<Item> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Item save(Item item) {
		return jpaRepository.save(item);
	}

	@Override
	public List<Item> saveAll(Iterable<Item> items) {
		return jpaRepository.saveAll(items);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
