package spring.web.java.domain.model.entity;

import java.util.ArrayList;
import java.util.List;

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
import spring.web.java.domain.converter.CryptoAttributeConverter;
import spring.web.java.domain.model.enums.MemberRole;

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
	private MemberRole role;

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

	public static Member create(String account, String password, String name, MemberRole role, Address address) {
		Member member = new Member();

		member.account = account;
		member.password = password;
		member.name = name;
		member.role = role;
		member.address = address;

		return member;
	}

	public void update(String name, MemberRole role, Address address) {
		this.name = name;
		this.role = role;
		this.address = address;
	}
}
