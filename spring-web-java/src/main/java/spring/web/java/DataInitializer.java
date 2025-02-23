package spring.web.java;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.infrastructure.persistence.PermissionJpaRepository;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

	private final PermissionJpaRepository permissionJpaRepository;

	@Override
	public void run(ApplicationArguments args) {
		permissionJpaRepository.saveAll(
			List.of(
				new Permission("ITEM_READ"),
				new Permission("ITEM_CREATE"),
				new Permission("ITEM_UPDATE"),
				new Permission("ITEM_DELETE")
			)
		);
	}
}
