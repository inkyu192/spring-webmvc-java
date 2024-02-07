package spring.web.kotlin.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.domain.Address
import spring.web.kotlin.domain.Member
import spring.web.kotlin.dto.request.MemberSaveRequest
import spring.web.kotlin.dto.request.MemberUpdateRequest
import spring.web.kotlin.dto.response.MemberResponse
import spring.web.kotlin.exception.CommonException
import spring.web.kotlin.repository.MemberRepository

@Service
@Transactional(readOnly = true)
class MemberService(
    val memberRepository: MemberRepository
) {

    @Transactional
    fun saveMember(memberSaveRequest: MemberSaveRequest): MemberResponse {
        memberRepository.findByAccount(memberSaveRequest.account)
            .ifPresent { member ->
                throw CommonException(ApiResponseCode.DATA_DUPLICATE)
            }

        val member = Member.create(
            account = memberSaveRequest.account,
            password = memberSaveRequest.password,
            name = memberSaveRequest.name,
            role = memberSaveRequest.role,
            address = Address.create(
                city = memberSaveRequest.city,
                street = memberSaveRequest.street,
                zipcode = memberSaveRequest.zipcode
            )
        )

        memberRepository.save(member)

        return MemberResponse(member)
    }

    fun findMembers(pageable: Pageable, account: String?, name: String?): Page<MemberResponse> {
        return memberRepository.findAllWithJpql(pageable, account, name)
            .map { MemberResponse(it) }
    }

    fun findMember(id: Long): MemberResponse {
        val member = memberRepository.findById(id)
            .orElseThrow { CommonException(ApiResponseCode.DATA_NOT_FOUND) }

        return MemberResponse(member)
    }

    @Transactional
    fun updateMember(id: Long, memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val member = memberRepository.findById(id)
            .orElseThrow { CommonException(ApiResponseCode.DATA_NOT_FOUND) }

        member.update(
            name = memberUpdateRequest.name,
            role = memberUpdateRequest.role,
            address = Address.create(
                city = memberUpdateRequest.city,
                street = memberUpdateRequest.street,
                zipcode = memberUpdateRequest.zipcode
            )
        )

        return MemberResponse(member)
    }

    @Transactional
    fun deleteMember(id: Long) {
        memberRepository.deleteById(id)
    }
}