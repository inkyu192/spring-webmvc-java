package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Item;
import spring.webmvc.domain.repository.ItemRepository;
import spring.webmvc.infrastructure.persistence.ItemJpaRepository;
import spring.webmvc.infrastructure.persistence.ItemQuerydslRepository;

@Component
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
	public List<Item> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}

	@Override
	public Item save(Item item) {
		return jpaRepository.save(item);
	}

	@Override
	public void delete(Item item) {
		jpaRepository.delete(item);
	}
}
