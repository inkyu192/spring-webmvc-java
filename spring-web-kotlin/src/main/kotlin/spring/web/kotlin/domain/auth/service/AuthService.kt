package spring.web.kotlin.domain.auth.service

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.member.dto.MemberLoginRequest
import spring.web.kotlin.domain.member.repository.MemberRepository
import spring.web.kotlin.domain.token.Token
import spring.web.kotlin.domain.token.dto.TokenRequest
import spring.web.kotlin.domain.token.dto.TokenResponse
import spring.web.kotlin.domain.token.repository.TokenRepository
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.config.JwtTokenProvider
import spring.web.kotlin.global.exception.BaseException

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
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED))

        if (!passwordEncoder.matches(memberLoginRequest.password, member.password)) {
            throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
        }

        val accessToken = jwtTokenProvider.createAccessToken(member.id!!, member.role)
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
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        if (token.refreshToken != tokenRequest.refreshToken) {
            throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
        }

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)

        return TokenResponse(jwtTokenProvider.createAccessToken(member.id!!, member.role), token.refreshToken)
    }
}