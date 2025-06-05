package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.infrastructure.config.DataJpaTestConfig;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class PermissionJpaRepositoryTest {

	@Autowired
	private PermissionJpaRepository permissionJpaRepository;

	@Test
	@DisplayName("save: Permission 저장 후 반환한다")
	void save() {
		// Given
		Permission permission = Permission.create("name");

		// When
		Permission saved = permissionJpaRepository.save(permission);

		// Then
		Assertions.assertThat(saved.getId()).isNotNull();
		Assertions.assertThat(saved.getName()).isEqualTo(permission.getName());
	}

	@Test
	@DisplayName("findById: Permission 반환한다")
	void findById() {
		// Given
		Permission permission = permissionJpaRepository.save(Permission.create("name"));

		// When
		Optional<Permission> result = permissionJpaRepository.findById(permission.getId());

		// Then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getName()).isEqualTo(permission.getName());
	}

	@Test
	@DisplayName("findAll: Permission 목록 반환한다")
	void findAll() {
		// Given
		permissionJpaRepository.save(Permission.create("name1"));
		permissionJpaRepository.save(Permission.create("name2"));

		// When
		List<Permission> result = permissionJpaRepository.findAll();

		// Then
		Assertions.assertThat(result).hasSize(2);
	}

	@Test
	@DisplayName("deleteById: Permission 삭제한다")
	void deleteById() {
		// Given
		Permission permission = permissionJpaRepository.save(Permission.create("name"));
		Long id = permission.getId();

		// When
		permissionJpaRepository.deleteById(id);

		// Then
		Optional<Permission> deleted = permissionJpaRepository.findById(id);
		Assertions.assertThat(deleted).isEmpty();
	}
}
