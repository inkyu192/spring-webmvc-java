package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

class EntityNotFoundException(clazz: Class<*>, id: Long) :
    AbstractHttpException("${clazz.simpleName} 엔티티를 찾을 수 없습니다. (ID: $id)", HttpStatus.NOT_FOUND)