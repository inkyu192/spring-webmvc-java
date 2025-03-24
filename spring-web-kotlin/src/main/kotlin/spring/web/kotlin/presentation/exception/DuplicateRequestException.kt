package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

class DuplicateRequestException(memberId: Long, method: String, uri: String) :
    AbstractHttpException("회원(ID: $memberId)의 요청이 중복되었습니다. ($method $uri)", HttpStatus.TOO_MANY_REQUESTS)