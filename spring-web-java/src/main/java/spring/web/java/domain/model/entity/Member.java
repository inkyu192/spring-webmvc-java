package spring.web.java.domain.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.domain.converter.CryptoAttributeConverter;

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

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberRole> roles = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberPermission> permissions = new ArrayList<>();

	public static Member create(String account, String password, String name, Address address) {
		Member member = new Member();

		member.account = account;
		member.password = password;
		member.name = name;
		member.address = address;

		return member;
	}

	public void addRole(MemberRole memberRole) {
		roles.add(memberRole);
		memberRole.assignToMember(this);
	}

	public void addPermission(MemberPermission memberPermission) {
		permissions.add(memberPermission);
		memberPermission.setMember(this);
	}

	public void update(String name, Address address) {
		this.name = name;
		this.address = address;
	}
}
