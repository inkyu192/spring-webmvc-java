package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<RolePermission> rolePermissions = new ArrayList<>();

	public List<RolePermission> getRolePermissions() {
		return Collections.unmodifiableList(rolePermissions);
	}

	public static Role create(String name) {
		Role role = new Role();

		role.name = name;

		return role;
	}

	public void addPermission(Permission permission) {
		RolePermission rolePermission = RolePermission.create(this, permission);

		rolePermissions.add(rolePermission);
	}
}
