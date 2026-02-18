package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.infrastructure.persistence.jpa.MenuJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.MenuQuerydslRepository;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {

	private final MenuJpaRepository jpaRepository;
	private final MenuQuerydslRepository querydslRepository;

	@Override
	public List<Menu> findAllById(List<Long> ids) {
		return jpaRepository.findAllById(ids);
	}

	@Override
	public List<Menu> findAllByPermissions(Iterable<String> permissions) {
		return querydslRepository.findAllByPermissions(permissions);
	}

	@Override
	public List<Menu> saveAll(Iterable<Menu> menus) {
		return jpaRepository.saveAll(menus);
	}
}
