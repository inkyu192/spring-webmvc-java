package spring.web.kotlin.application.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.application.event.NotificationEvent
import spring.web.kotlin.domain.model.entity.Address
import spring.web.kotlin.domain.model.entity.Member
import spring.web.kotlin.domain.model.entity.MemberPermission
import spring.web.kotlin.domain.model.entity.MemberRole
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.PermissionRepository
import spring.web.kotlin.domain.repository.RoleRepository
import spring.web.kotlin.presentation.exception.ErrorCode
import spring.web.kotlin.infrastructure.util.SecurityContextUtil
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.dto.request.MemberSaveRequest
import spring.web.kotlin.presentation.dto.request.MemberUpdateRequest
import spring.web.kotlin.presentation.dto.response.MemberResponse

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
            throw BaseException(ErrorCode.INVALID_INPUT_VALUE, HttpStatus.BAD_REQUEST)
        }

        memberRepository.findByAccount(account)?.let {
            throw BaseException(ErrorCode.DUPLICATE_DATA, HttpStatus.CONFLICT)
        }

        val address = Address.create(
            city = city,
            street = street,
            zipcode = zipcode,
        )

        val memberRoles = roleIds?.map {
            val role = roleRepository.findByIdOrNull(it)
                ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

            MemberRole.create(role)
        }

        val memberPermissions = permissionIds?.map {
            val permission = permissionRepository.findByIdOrNull(it)
                ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

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
        val member = memberRepository.findByIdOrNull(SecurityContextUtil.getMemberId())
            ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        return MemberResponse(member)
    }

    @Transactional
    fun updateMember(memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val member = memberRepository.findByIdOrNull(SecurityContextUtil.getMemberId())
            ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

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
        memberRepository.deleteById(SecurityContextUtil.getMemberId())
    }
}
