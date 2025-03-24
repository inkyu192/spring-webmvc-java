package spring.web.kotlin.infrastructure.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.model.enums.Category
import spring.web.kotlin.infrastructure.config.DataJpaTestConfig

@DataJpaTest
@Import(DataJpaTestConfig::class)
class ItemQuerydslRepositoryTest(
    jpaQueryFactory: JPAQueryFactory,
    private val itemJpaRepository: ItemJpaRepository,
) : DescribeSpec({
    val itemQuerydslRepository = ItemQuerydslRepository(jpaQueryFactory)

    describe("findAll 은") {
        it("name 을 필털링하고 페이징 되어서 조회한다") {
            val request = listOf(
                Item.create("이름1", "설명1", 150, 1, Category.ROLE_BOOK),
                Item.create("이름2", "설명2", 160, 2, Category.ROLE_BOOK),
                Item.create("이름3", "설명3", 170, 3, Category.ROLE_BOOK),
                Item.create("이름4", "설명4", 180, 4, Category.ROLE_TICKET),
                Item.create("이5", "설명5", 190, 5, Category.ROLE_TICKET)
            )
            itemJpaRepository.saveAll(request)

            val pageable: Pageable = PageRequest.of(0, 3)
            val name = "이름"

            itemQuerydslRepository.findAll(pageable, name).apply {
                number shouldBe pageable.pageNumber
                size shouldBe pageable.pageSize
                totalElements shouldBe request.filter { it.name.contains(name) }.size.toLong()
            }
        }
    }
})
