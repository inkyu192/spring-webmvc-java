package spring.web.kotlin.service

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
    fun saveMember(memberSaveRequest: MemberSaveRequest) =
        memberRepository.findByAccount(memberSaveRequest.account).orElse(null)
            ?.also { throw CommonException(ApiResponseCode.DATA_DUPLICATE) }
            ?: Member.create(
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
                .also { memberRepository.save(it) }
                .let { MemberResponse(it) }

    fun findMembers(pageable: Pageable, account: String?, name: String?) =
        memberRepository.findAllWithJpql(pageable, account, name)
            .map { MemberResponse(it) }

    fun findMember(id: Long) = memberRepository.findById(id).orElse(null)
        ?.let { MemberResponse(it) }
        ?: CommonException(ApiResponseCode.DATA_NOT_FOUND)

    @Transactional
    fun updateMember(id: Long, memberUpdateRequest: MemberUpdateRequest) =
        memberRepository.findById(id).orElse(null)
            ?.let {
                it.update(
                    name = memberUpdateRequest.name,
                    role = memberUpdateRequest.role,
                    address = Address.create(
                        city = memberUpdateRequest.city,
                        street = memberUpdateRequest.street,
                        zipcode = memberUpdateRequest.zipcode
                    )
                )

                MemberResponse(it)
            }
            ?: CommonException(ApiResponseCode.DATA_NOT_FOUND)

    @Transactional
    fun deleteMember(id: Long) = memberRepository.deleteById(id)
}