package spring.web.java.domain.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

	@Convert(converter = CryptoAttributeConverter.class)
	private String phone;

	private LocalDate birthDate;

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
		List<MemberRole> memberRoles,
		List<MemberPermission> memberPermissions
	) {
		Member member = new Member();

		member.account = account;
		member.password = password;
		member.name = name;

		memberRoles.forEach(member::associateRole);
		memberPermissions.forEach(member::associatePermission);

		return member;
	}

	public void associateRole(MemberRole memberRole) {
		memberRoles.add(memberRole);
		memberRole.associateMember(this);
	}

	public void associatePermission(MemberPermission memberPermission) {
		memberPermissions.add(memberPermission);
		memberPermission.associateMember(this);
	}

	public void update(String password, String name, String phone, LocalDate birthDate) {
		if (StringUtils.hasText(password)) {
			this.password = password;
		}

		if (StringUtils.hasText(name)) {
			this.name = name;
		}

		if (StringUtils.hasText(phone)) {
			this.phone = phone;
		}

		if (birthDate != null) {
			this.birthDate = birthDate;
		}
	}
}
