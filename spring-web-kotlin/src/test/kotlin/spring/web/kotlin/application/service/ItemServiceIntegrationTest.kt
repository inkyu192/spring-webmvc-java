package spring.web.kotlin.domain.item.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.http.HttpStatus
import spring.web.kotlin.application.service.ItemService
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.model.enums.Category
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.presentation.dto.request.ItemSaveRequest
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode

@DataJpaTest
class ItemServiceIntegrationTest(
    private val itemRepository: ItemRepository,
) : DescribeSpec({
    val itemService = ItemService(itemRepository)

    describe("findItem 메서드는") {
        context("데이터가 없을 때") {
            it("BaseException 예외 발생 한다") {
                shouldThrow<BaseException> { itemService.findItem(0) }.apply {
                    httpStatus shouldBe HttpStatus.NOT_FOUND
                    errorCode shouldBe ErrorCode.DATA_NOT_FOUND
                }
            }
        }

        context("데이터가 있을 때") {
            val item = Item.create("이름", "설명", 100, 1, Category.ROLE_BOOK).also {
                itemRepository.save(it)
            }

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
                val request = ItemSaveRequest("이름", "설명", 100, 1, Category.ROLE_BOOK).also {
                    itemService.putItem(1, it)
                }

                itemRepository.findByIdOrNull(1)?.apply {
                    name shouldBe request.name
                    description shouldBe request.description
                    price shouldBe request.price
                    quantity shouldBe request.quantity
                }
            }
        }

        context("데이터가 있을 때") {
            val item = Item.create("이름", "설명", 100, 1, Category.ROLE_BOOK).also {
                itemRepository.save(it)
            }

            it("수정 되어야 한다") {
                val request = ItemSaveRequest("이름2", "설명2", 200, 2, Category.ROLE_TICKET).also {
                    itemService.putItem(item.id!!, it)
                }

                itemRepository.findByIdOrNull(item.id!!)?.apply {
                    name shouldBe request.name
                    description shouldBe request.description
                    price shouldBe request.price
                    quantity shouldBe request.quantity
                }
            }
        }
    }
})
