package spring.web.kotlin.domain.item.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spring.web.kotlin.domain.item.Item
import spring.web.kotlin.domain.item.repository.ItemRepository
import spring.web.kotlin.global.TestConfig

@DataJpaTest
@Import(TestConfig::class)
class ItemServiceTest(
    val itemRepository: ItemRepository,
) : BehaviorSpec({
    val itemService = ItemService(itemRepository)

    given("저장된 상품이 있을 경우") {
        val savedItem = itemRepository.save(
            Item.create("이름", "설명", 15000, 10, Item.Category.ROLE_BOOK)
        )

        When("상품을 조회 하면") {
            val findItem = itemService.findItem(savedItem.id!!)

            Then("조회가 된다") {
                savedItem.id shouldBe findItem.id
            }
        }
    }
})
