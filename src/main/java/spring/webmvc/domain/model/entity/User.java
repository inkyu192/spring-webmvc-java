package spring.webmvc.domain.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.CryptoAttributeConverter;
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Phone;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = CryptoAttributeConverter.class)
	private String name;

	@Embedded
	private Phone phone;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private LocalDate birthday;

	private String profileImage;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<UserRole> userRoles = new ArrayList<>();

	public List<UserRole> getUserRoles() {
		return Collections.unmodifiableList(userRoles);
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<UserPermission> userPermissions = new ArrayList<>();

	public List<UserPermission> getUserPermissions() {
		return Collections.unmodifiableList(userPermissions);
	}

	public static User create(String name, Phone phone, Gender gender, LocalDate birthday, String profileImage) {
		User user = new User();

		user.name = name;
		user.phone = phone;
		user.gender = gender;
		user.birthday = birthday;
		user.profileImage = profileImage;

		return user;
	}

	public void addUserRole(Role role) {
		UserRole userRole = UserRole.create(this, role);
		userRoles.add(userRole);
	}

	public void addUserPermission(Permission permission) {
		UserPermission userPermission = UserPermission.create(this, permission);
		userPermissions.add(userPermission);
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public List<String> getPermissionNames() {
		List<String> rolePermissions = userRoles.stream()
			.flatMap(it -> it.getRole().getRolePermissions().stream())
			.map(it -> it.getPermission().getName())
			.toList();

		List<String> directPermissions = userPermissions.stream()
			.map(up -> up.getPermission().getName())
			.toList();

		return Stream.concat(rolePermissions.stream(), directPermissions.stream())
			.distinct()
			.toList();
	}
}
