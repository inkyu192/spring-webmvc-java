package spring.web.kotlin.infrastructure.aspect

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestLock()
