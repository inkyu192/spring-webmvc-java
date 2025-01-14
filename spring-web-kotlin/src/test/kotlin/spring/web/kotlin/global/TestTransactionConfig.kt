package spring.web.kotlin.global

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode

class TestTransactionConfig: AbstractProjectConfig() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))
}