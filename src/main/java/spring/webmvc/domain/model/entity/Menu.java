package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTime {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String path;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Menu parent;

	@OneToMany(mappedBy = "parent")
	private List<Menu> children = new ArrayList<>();

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
	private List<MenuPermission> menuPermissions = new ArrayList<>();

	public List<Menu> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public List<MenuPermission> getMenuPermissions() {
		return Collections.unmodifiableList(menuPermissions);
	}

	public static Menu create(String name, String path) {
		Menu menu = new Menu();

		menu.name = name;
		menu.path = path;

		return menu;
	}

	public void updateParent(Menu parent) {
		this.parent = parent;
		parent.children.add(this);
	}

	public void addPermission(Permission permission) {
		menuPermissions.add(MenuPermission.create(this, permission));
	}
}
