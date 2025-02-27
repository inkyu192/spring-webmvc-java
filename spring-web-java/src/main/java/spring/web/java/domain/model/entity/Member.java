package spring.web.java.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
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

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberRole> memberRoles = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberPermission> memberPermissions = new ArrayList<>();

	public List<MemberRole> getMemberRoles() {
		return Collections.unmodifiableList(memberRoles);
	}

	public List<MemberPermission> getMemberPermissions() {
		return Collections.unmodifiableList(memberPermissions);
	}

	public static Member create(
		String account,
		String password,
		String name,
		Address address,
		List<MemberRole> memberRoles,
		List<MemberPermission> memberPermissions
	) {
		Member member = new Member();

		member.account = account;
		member.password = password;
		member.name = name;
		member.address = address;

		memberRoles.forEach(member::associateRole);
		memberPermissions.forEach(member::associatePermission);

		return member;
	}

	public void associateRole(MemberRole memberRole) {
		memberRoles.add(memberRole);
		memberRole.setMember(this);
	}

	public void associatePermission(MemberPermission memberPermission) {
		memberPermissions.add(memberPermission);
		memberPermission.setMember(this);
	}

	public void update(String name, Address address) {
		this.name = name;
		this.address = address;
	}
}
