package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

abstract class AbstractValidationException(message: String) : AbstractHttpException(message, HttpStatus.BAD_REQUEST)