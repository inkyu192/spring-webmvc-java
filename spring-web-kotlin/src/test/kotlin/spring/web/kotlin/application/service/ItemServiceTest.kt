package spring.web.kotlin.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.model.enums.Category
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.presentation.dto.request.ItemSaveRequest
import spring.web.kotlin.presentation.exception.EntityNotFoundException

class ItemServiceTest : DescribeSpec({
    val itemRepository = mockk<ItemRepository>()
    val itemService = ItemService(itemRepository)

    describe("saveItem 은") {
        it("한개를 저장한다") {
            val request = ItemSaveRequest(
                name = "item1",
                description = "description",
                price = 1000,
                quantity = 10,
                category = Category.ROLE_TICKET
            )

            val item = spyk(
                Item.create(
                    name = request.name,
                    description = request.description,
                    price = request.price,
                    quantity = request.quantity,
                    category = request.category
                )
            ).apply { every { id } returns 1L }

            every { itemRepository.save(any()) } returns item

            itemService.saveItem(request).apply {
                name shouldBe request.name
                description shouldBe request.description
                price shouldBe request.price
                quantity shouldBe request.quantity
            }
        }
    }

    describe("findItems 은") {
        it("여러개를 조회한다") {
            val pageable = PageRequest.of(0, 10)
            val requestName = "Item"
            val items = listOf(
                spyk(
                    Item.create(
                        name = "item1",
                        description = "description1",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns 1L },
                spyk(
                    Item.create(
                        name = "item2",
                        description = "description2",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns 2L },
                spyk(
                    Item.create(
                        name = "item3",
                        description = "description3",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns 3L }
            )
            val page = PageImpl(items, pageable, items.size.toLong())

            every { itemRepository.findAll(pageable, requestName) } returns page

            itemService.findItems(pageable, requestName).apply {
                totalElements shouldBe items.size
                map { it.id }.toList() shouldBe listOf(1L, 2L, 3L)
            }
        }
    }

    describe("findItem 은") {
        val itemId = 5L

        context("데이터가 없을 경우") {
            it("EntityNotFoundException 던진다") {
                every { itemRepository.findByIdOrNull(itemId) } returns null

                shouldThrow<EntityNotFoundException> { itemService.findItem(itemId) }.apply {
                    httpStatus shouldBe HttpStatus.NOT_FOUND
                }
            }
        }

        context("데이터가 있을 경우") {
            it("조회한다") {
                val item = spyk(
                    Item.create(
                        name = "item1",
                        description = "description",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns itemId }

                every { itemRepository.findByIdOrNull(itemId) } returns item

                itemService.findItem(itemId).apply {
                    id shouldBe itemId
                }
            }
        }
    }

    describe("putItem 은") {
        val itemId = 5L
        val request = ItemSaveRequest(
            name = "item2",
            description = "description2",
            price = 10000,
            quantity = 100,
            category = Category.ROLE_TICKET
        )

        context("데이터가 없을 경우") {
            it("생성한다") {
                val item = spyk(
                    Item.create(
                        name = request.name,
                        description = request.description,
                        price = request.price,
                        quantity = request.quantity,
                        category = request.category
                    )
                ).apply { every { id } returns 1L }

                every { itemRepository.findByIdOrNull(itemId) } returns null
                every { itemRepository.save(any()) } returns item

                itemService.putItem(itemId, request).apply {
                    first shouldBe true
                    second.name shouldBe request.name
                    second.description shouldBe request.description
                    second.price shouldBe request.price
                    second.quantity shouldBe request.quantity
                }
            }
        }

        context("데이터가 있을 경우") {
            it("수정한다") {
                val item = spyk(
                    Item.create(
                        name = "item1",
                        description = "description",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns itemId }

                every { itemRepository.findByIdOrNull(itemId) } returns item

                itemService.putItem(itemId, request).apply {
                    first shouldBe false
                    second.name shouldBe request.name
                    second.description shouldBe request.description
                    second.price shouldBe request.price
                    second.quantity shouldBe request.quantity
                }
            }
        }
    }

    describe("deleteItem 은") {
        val itemId = 5L

        context("데이터가 없을 경우") {
            it("EntityNotFoundException 던진다") {
                every { itemRepository.findByIdOrNull(itemId) } returns null

                shouldThrow<EntityNotFoundException> { itemService.deleteItem(itemId) }.apply {
                    httpStatus shouldBe HttpStatus.NOT_FOUND
                }
            }
        }

        context("데이터가 있을 경우") {
            it("삭제한다") {
                val item = spyk(
                    Item.create(
                        name = "item1",
                        description = "description",
                        price = 1000,
                        quantity = 10,
                        category = Category.ROLE_BOOK
                    )
                ).apply { every { id } returns itemId }

                every { itemRepository.findByIdOrNull(itemId) } returns item
                every { itemRepository.delete(item) } returns Unit

                itemService.deleteItem(itemId)

                verify(exactly = 1) { itemRepository.delete(item) }
            }
        }
    }
})
