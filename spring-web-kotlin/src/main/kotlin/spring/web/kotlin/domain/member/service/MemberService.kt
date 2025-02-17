package spring.web.kotlin.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.Address
import spring.web.kotlin.domain.member.Member
import spring.web.kotlin.domain.member.dto.MemberResponse
import spring.web.kotlin.domain.member.dto.MemberSaveRequest
import spring.web.kotlin.domain.member.dto.MemberUpdateRequest
import spring.web.kotlin.domain.member.repository.MemberRepository
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.common.SecurityUtils
import spring.web.kotlin.global.exception.BaseException

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun saveMember(memberSaveRequest: MemberSaveRequest): MemberResponse {
        memberRepository.findByAccount(memberSaveRequest.account)?.let {
            throw BaseException(ResponseMessage.DUPLICATE_DATA, HttpStatus.CONFLICT)
        }

        val member = memberRepository.save(
            Member.create(
                account = memberSaveRequest.account,
                password = passwordEncoder.encode(memberSaveRequest.password),
                name = memberSaveRequest.name,
                role = memberSaveRequest.role,
                address = Address.create(
                    city = memberSaveRequest.city,
                    street = memberSaveRequest.street,
                    zipcode = memberSaveRequest.zipcode,
                ),
            ),
        )

        return MemberResponse(member)
    }

    fun findMember(): MemberResponse {
        val id = SecurityUtils.getMemberId()
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        val member = memberRepository.findByIdOrNull(id)
            ?: throw BaseException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        return MemberResponse(member)
    }

    @Transactional
    fun patchMember(memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val id = SecurityUtils.getMemberId()
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        val member = memberRepository.findByIdOrNull(id)
            ?: throw BaseException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        member.update(
            name = memberUpdateRequest.name,
            role = memberUpdateRequest.role,
            address =
            Address.create(
                city = memberUpdateRequest.city,
                street = memberUpdateRequest.street,
                zipcode = memberUpdateRequest.zipcode,
            ),
        )

        return MemberResponse(member)
    }

    @Transactional
    fun deleteMember() {
        val id = SecurityUtils.getMemberId()
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        memberRepository.deleteById(id)
    }
}
