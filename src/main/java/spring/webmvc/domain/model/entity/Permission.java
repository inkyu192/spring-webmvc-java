package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission extends BaseTime {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PermissionMenu> permissionMenus = new ArrayList<>();

	public List<PermissionMenu> getPermissionMenus() {
		return Collections.unmodifiableList(permissionMenus);
	}

	public static Permission create(String name) {
		Permission permission = new Permission();
		permission.name = name;
		return permission;
	}

	public void addMenu(Menu menu) {
		permissionMenus.add(PermissionMenu.create(menu, this));
		menu.addPermission(this);
	}
}
