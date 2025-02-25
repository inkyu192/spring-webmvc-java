package spring.web.kotlin.application.service

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Member
import spring.web.kotlin.domain.model.entity.Token
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.TokenRepository
import spring.web.kotlin.presentation.exception.ErrorCode
import spring.web.kotlin.infrastructure.config.security.JwtTokenProvider
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.dto.request.MemberLoginRequest
import spring.web.kotlin.presentation.dto.request.TokenRequest
import spring.web.kotlin.presentation.dto.response.TokenResponse

@Service
@Transactional(readOnly = true)
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val tokenRepository: TokenRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun login(memberLoginRequest: MemberLoginRequest): TokenResponse {
        val member = (memberRepository.findByAccount(memberLoginRequest.account)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED))

        if (!passwordEncoder.matches(memberLoginRequest.password, member.password)) {
            throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
        }

        val accessToken = jwtTokenProvider.createAccessToken(member.id!!, getPermissions(member))
        val refreshToken = jwtTokenProvider.createRefreshToken()

        tokenRepository.save(Token.create(member.id!!, refreshToken))

        return TokenResponse(accessToken, refreshToken)
    }

    fun refreshToken(tokenRequest: TokenRequest): TokenResponse {
        val claims = runCatching { jwtTokenProvider.parseAccessToken(tokenRequest.accessToken) }
            .getOrElse { throwable ->
                when (throwable) {
                    is ExpiredJwtException -> throwable.claims
                    else -> throw throwable
                }
            }

        jwtTokenProvider.parseRefreshToken(tokenRequest.refreshToken)

        val memberId = claims["memberId"] as Long

        val token = tokenRepository.findByIdOrNull(memberId)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        if (token.refreshToken != tokenRequest.refreshToken) {
            throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
        }

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        return TokenResponse(
            jwtTokenProvider.createAccessToken(member.id!!, getPermissions(member)),
            token.refreshToken
        )
    }

    private fun getPermissions(member: Member): List<String> {
        val rolePermissions = member.memberRoles
            .flatMap { it.role.rolePermissions }
            .map { it.permission.name }

        val directPermissions = member.memberPermissions
            .map { it.permission.name }

        return (rolePermissions + directPermissions).distinct()
    }
}