package spring.web.java.domain.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Address;
import spring.web.java.domain.Base;
import spring.web.java.domain.order.Order;
import spring.web.java.global.converter.persistence.CryptoAttributeConverter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Base {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String account;
	private String password;

	@Convert(converter = CryptoAttributeConverter.class)
	private String name;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

	public static Member create(String account, String password, String name, Role role, Address address) {
		Member member = new Member();

		member.account = account;
		member.password = password;
		member.name = name;
		member.role = role;
		member.address = address;

		return member;
	}

	public void update(String name, Role role, Address address) {
		this.name = name;
		this.role = role;
		this.address = address;
	}

	@Getter
	@RequiredArgsConstructor
	public enum Role {

		ROLE_ADMIN("어드민"),
		ROLE_BUYER("소비자"),
		ROLE_SELLER("판매자");

		private final String description;

		@JsonCreator
		public static Role of(Object name) {
			return Arrays.stream(Role.values())
				.filter(role -> role.name().equals(name))
				.findFirst()
				.orElse(null);
		}
	}
}
