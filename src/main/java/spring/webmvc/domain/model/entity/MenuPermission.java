package spring.webmvc.domain.model.entity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
public class MenuPermission {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Permission permission;

	public static MenuPermission create(Menu menu, Permission permission) {
		MenuPermission menuPermission = new MenuPermission();

		menuPermission.menu = menu;
		menuPermission.permission = permission;

		return menuPermission;
	}
}
