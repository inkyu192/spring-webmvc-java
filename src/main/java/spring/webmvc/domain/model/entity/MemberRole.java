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
public class MemberRole {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Role role;

	public static MemberRole create(Member member, Role role) {
		MemberRole memberRole = new MemberRole();

		memberRole.member = member;
		memberRole.role = role;

		return memberRole;
	}
}
