package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

abstract class ValidationFailedException(message: String) : BusinessException(message, HttpStatus.BAD_REQUEST)