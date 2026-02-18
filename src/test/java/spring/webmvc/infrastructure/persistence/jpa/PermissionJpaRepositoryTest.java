package spring.webmvc.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.infrastructure.config.RepositoryTest;

@RepositoryTest
class PermissionJpaRepositoryTest {

	@Autowired
	private PermissionJpaRepository permissionJpaRepository;

	@Test
	@DisplayName("save: Permission 저장 후 반환한다")
	void save() {
		Permission permission = Permission.create("name");

		Permission saved = permissionJpaRepository.save(permission);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo(permission.getName());
	}

	@Test
	@DisplayName("findById: Permission 반환한다")
	void findById() {
		Permission permission = permissionJpaRepository.save(Permission.create("name"));

		Optional<Permission> result = permissionJpaRepository.findById(permission.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo(permission.getName());
	}

	@Test
	@DisplayName("findAll: Permission 목록 반환한다")
	void findAll() {
		permissionJpaRepository.save(Permission.create("name1"));
		permissionJpaRepository.save(Permission.create("name2"));

		List<Permission> result = permissionJpaRepository.findAll();

		assertThat(result).hasSize(2);
	}

	@Test
	@DisplayName("deleteById: Permission 삭제한다")
	void deleteById() {
		Permission permission = permissionJpaRepository.save(Permission.create("name"));
		Long id = permission.getId();

		permissionJpaRepository.deleteById(id);

		Optional<Permission> deleted = permissionJpaRepository.findById(id);

		assertThat(deleted).isEmpty();
	}
}
