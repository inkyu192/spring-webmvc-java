package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class Role extends BaseTime {

	@Id
	@GeneratedValue
	@Column(name = "role_id")
	private Long id;
	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
	private List<RolePermission> rolePermissions = new ArrayList<>();

	public List<RolePermission> getRolePermissions() {
		return Collections.unmodifiableList(rolePermissions);
	}

	public static Role create(String name, List<RolePermission> rolePermissions) {
		Role role = new Role();

		role.name = name;
		rolePermissions.forEach(role::associatePermission);

		return role;
	}

	public void associatePermission(RolePermission rolePermission) {
		rolePermissions.add(rolePermission);
		rolePermission.associateRole(this);
	}
}
