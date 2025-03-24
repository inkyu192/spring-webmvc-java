package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

class DuplicateEntityException(clazz: Class<*>, name: String) :
    AbstractHttpException("이미 존재하는 ${clazz.simpleName} 엔티티입니다. (ID: '$name')", HttpStatus.CONFLICT)