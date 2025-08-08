package spring.webmvc;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

	private final MenuRepository menuRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Override
	public void run(ApplicationArguments args) {
		// 메뉴 생성
		Menu product = Menu.create("상품 관리", "/products");
		Menu order = Menu.create("주문 관리", "/orders");
		Menu settings = Menu.create("설정", null);
		Menu permissionManage = Menu.create("권한 관리", "/permissions", settings);
		Menu menuManage = Menu.create("메뉴 관리", "/menus", settings);
		Menu roleManage = Menu.create("역할 관리", "/roles", settings);

		menuRepository.saveAll(
			List.of(
				product,
				order,
				settings,
				permissionManage,
				menuManage,
				roleManage
			)
		);


		// 권한 생성
		Permission productReader = permissionRepository.save(Permission.create("PRODUCT_READER"));
		productReader.addMenu(product);
		Permission productWriter = permissionRepository.save(Permission.create("PRODUCT_WRITER"));
		Permission orderReader = permissionRepository.save(Permission.create("ORDER_READER"));
		orderReader.addMenu(order);
		Permission orderWriter = permissionRepository.save(Permission.create("ORDER_WRITER"));
		Permission permissionReader = permissionRepository.save(Permission.create("PERMISSION_READER"));
		permissionReader.addMenu(settings);
		permissionReader.addMenu(permissionManage);
		Permission permissionWriter = permissionRepository.save(Permission.create("PERMISSION_WRITER"));
		Permission menuReader = permissionRepository.save(Permission.create("MENU_READER"));
		menuReader.addMenu(settings);
		menuReader.addMenu(menuManage);
		Permission menuWriter = permissionRepository.save(Permission.create("MENU_WRITER"));
		Permission roleReader = permissionRepository.save(Permission.create("ROLE_READER"));
		roleReader.addMenu(settings);
		roleReader.addMenu(roleManage);
		Permission roleWriter = permissionRepository.save(Permission.create("ROLE_WRITER"));

		permissionRepository.saveAll(
			List.of(
				productReader,
				productWriter,
				orderReader,
				orderWriter,
				permissionReader,
				permissionWriter,
				menuReader,
				menuWriter,
				roleReader,
				roleWriter
			)
		);

		// 역할 생성
		Role roleViewer = Role.create("ROLE_VIEWER");
		roleViewer.addPermission(productReader);
		roleViewer.addPermission(orderReader);
		roleViewer.addPermission(permissionReader);
		roleViewer.addPermission(menuReader);
		roleViewer.addPermission(roleReader);

		Role roleProductManager = Role.create("ROLE_PRODUCT_MANAGER");
		roleProductManager.addPermission(productReader);
		roleProductManager.addPermission(productWriter);

		Role roleAdmin = Role.create("ROLE_ADMIN");
		roleAdmin.addPermission(productReader);
		roleAdmin.addPermission(productWriter);
		roleAdmin.addPermission(orderReader);
		roleAdmin.addPermission(orderWriter);
		roleAdmin.addPermission(permissionReader);
		roleAdmin.addPermission(permissionWriter);
		roleAdmin.addPermission(menuReader);
		roleAdmin.addPermission(menuWriter);
		roleAdmin.addPermission(roleReader);
		roleAdmin.addPermission(roleWriter);

		roleRepository.saveAll(List.of(roleViewer, roleProductManager, roleAdmin));
	}
}
