package spring.web.kotlin.application.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.application.event.NotificationEvent
import spring.web.kotlin.domain.model.entity.*
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.PermissionRepository
import spring.web.kotlin.domain.repository.RoleRepository
import spring.web.kotlin.infrastructure.util.SecurityContextUtil
import spring.web.kotlin.presentation.dto.request.MemberSaveRequest
import spring.web.kotlin.presentation.dto.request.MemberUpdateRequest
import spring.web.kotlin.presentation.dto.response.MemberResponse
import spring.web.kotlin.presentation.exception.AtLeastOneRequiredException
import spring.web.kotlin.presentation.exception.DuplicateEntityException
import spring.web.kotlin.presentation.exception.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun saveMember(memberSaveRequest: MemberSaveRequest): MemberResponse {
        val (account, password, name, city, street, zipcode, roleIds, permissionIds) = memberSaveRequest

        if (roleIds.isNullOrEmpty() && permissionIds.isNullOrEmpty()) {
            throw AtLeastOneRequiredException("roleIds", "permissionIds")
        }

        memberRepository.findByAccount(account)?.let {
            throw DuplicateEntityException(Member::class.java, account)
        }

        val address = Address.create(
            city = city,
            street = street,
            zipcode = zipcode,
        )

        val memberRoles = roleIds?.map {
            val role = roleRepository.findByIdOrNull(it)
                ?: throw EntityNotFoundException(Role::class.java, it)

            MemberRole.create(role)
        }

        val memberPermissions = permissionIds?.map {
            val permission = permissionRepository.findByIdOrNull(it)
                ?: throw EntityNotFoundException(Permission::class.java, it)

            MemberPermission.create(permission)
        }

        val member = Member.create(
            account = account,
            password = passwordEncoder.encode(password),
            name = name,
            address = address,
            memberRoles = memberRoles,
            memberPermissions = memberPermissions
        )

        memberRepository.save(member)

        eventPublisher.publishEvent(
            NotificationEvent(
                member.id!!,
                "회원가입 완료",
                "회원가입을 환영합니다!",
                "/test/123"
            )
        )

        return MemberResponse(member)
    }

    fun findMember(): MemberResponse {
        val memberId = SecurityContextUtil.getMemberId()
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw EntityNotFoundException(Member::class.java, memberId)

        return MemberResponse(member)
    }

    @Transactional
    fun updateMember(memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val memberId = SecurityContextUtil.getMemberId()
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw EntityNotFoundException(Member::class.java, memberId)

        member.update(
            name = memberUpdateRequest.name,
            address = Address.create(
                city = memberUpdateRequest.city,
                street = memberUpdateRequest.street,
                zipcode = memberUpdateRequest.zipcode,
            ),
        )

        return MemberResponse(member)
    }

    @Transactional
    fun deleteMember() {
        val memberId = SecurityContextUtil.getMemberId()
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw EntityNotFoundException(Member::class.java, memberId)

        memberRepository.delete(member)
    }
}
