package spring.web.java.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.ItemJpaCustomRepositoryPort;
import spring.web.java.application.port.out.ItemJpaRepositoryPort;
import spring.web.java.application.port.out.ItemQueryDslRepositoryPort;
import spring.web.java.application.port.out.ItemRepositoryPort;
import spring.web.java.domain.Item;

@Component
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

	private final ItemJpaRepositoryPort jpaRepository;
	private final ItemJpaCustomRepositoryPort jpaCustomRepository;
	private final ItemQueryDslRepositoryPort queryDslRepository;

	@Override
	public Item save(Item item) {
		return jpaRepository.save(item);
	}

	@Override
	public Page<Item> findAll(Pageable pageable, String name) {
		// return jpaCustomRepository.findAll(pageable, name);
		return queryDslRepository.findAll(pageable, name);
	}

	@Override
	public Optional<Item> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
