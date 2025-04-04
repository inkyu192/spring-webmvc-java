package spring.webmvc.domain.model.entity;

import jakarta.persistence.Column;
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
public class MemberPermission {

	@Id
	@GeneratedValue
	@Column(name = "member_permission_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Permission permission;

	public static MemberPermission create(Permission permission) {
		MemberPermission memberPermission = new MemberPermission();

		memberPermission.permission = permission;

		return memberPermission;
	}

	public void associateMember(Member member) {
		this.member = member;
	}
}
