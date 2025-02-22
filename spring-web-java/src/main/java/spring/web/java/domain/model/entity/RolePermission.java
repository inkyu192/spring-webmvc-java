package spring.web.java.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermission extends Base {

	@Id
	@GeneratedValue
	@Column(name = "role_permission_id")
	private Long id;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id")
	private Permission permission;

	public static RolePermission create(Permission permission) {
		RolePermission rolePermission = new RolePermission();

		rolePermission.permission = permission;

		return rolePermission;
	}
}
