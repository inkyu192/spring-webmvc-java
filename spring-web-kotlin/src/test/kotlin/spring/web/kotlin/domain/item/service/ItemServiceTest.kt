package spring.web.kotlin.domain.item.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import spring.web.kotlin.domain.item.Item
import spring.web.kotlin.domain.item.dto.ItemSaveRequest
import spring.web.kotlin.domain.item.repository.ItemRepository
import spring.web.kotlin.global.TestConfig
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.exception.DomainException

@DataJpaTest
@Import(TestConfig::class)
class ItemServiceTest(
    private val itemRepository: ItemRepository,
    private val itemService: ItemService = ItemService(itemRepository)
) : DescribeSpec({
    describe("findItem 메서드는") {
        context("데이터가 없을 때") {
            it("DomainException 예외 발생 한다") {
                shouldThrow<DomainException> { itemService.findItem(0) }.apply {
                    httpStatus shouldBe HttpStatus.NOT_FOUND
                    responseMessage shouldBe ResponseMessage.DATA_NOT_FOUND
                }
            }
        }

        context("데이터가 있을 때") {
            val item = itemRepository.save(Item.create("이름", "설명", 100, 1, Item.Category.ROLE_BOOK))

            it("조회 되어야 한다") {
                itemService.findItem(item.id!!).apply {
                    name shouldBe item.name
                    description shouldBe item.description
                    price shouldBe item.price
                    quantity shouldBe item.quantity
                }
            }
        }
    }

    describe("putItem 메서드는") {
        context("데이터가 없을 때") {
            it("저장 되어야 한다") {
                val request = ItemSaveRequest("이름", "설명", 100, 1, Item.Category.ROLE_BOOK)
                itemService.putItem(1, request)

                itemRepository.findByIdOrNull(1)?.apply {
                    name shouldBe request.name
                    description shouldBe request.description
                    price shouldBe request.price
                    quantity shouldBe request.quantity
                }
            }
        }

        context("데이터가 있을 때") {
            val savedItem = itemRepository.save(Item.create("이름", "설명", 100, 1, Item.Category.ROLE_BOOK))

            it("수정 되어야 한다") {
                val request = ItemSaveRequest("이름2", "설명2", 200, 2, Item.Category.ROLE_TICKET)
                itemService.putItem(savedItem.id!!, request)

                itemRepository.findByIdOrNull(savedItem.id)?.apply {
                    name shouldBe request.name
                    description shouldBe request.description
                    price shouldBe request.price
                    quantity shouldBe request.quantity
                }
            }
        }
    }
})
