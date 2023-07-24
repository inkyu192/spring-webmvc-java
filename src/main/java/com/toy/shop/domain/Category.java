package com.toy.shop.domain;

import com.toy.shop.business.category.dto.CategoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseDomain {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    private String description;

//    @OneToMany(mappedBy = "category")
//    private List<Item> items = new ArrayList<>();

    public static Category createCategory(CategoryDto.Save requestDto) {
        Category category = new Category();
        category.name = requestDto.getName();
        category.description = requestDto.getDescription();

        return category;
    }

    public void updateCategory(CategoryDto.Update requestDto) {
        if (StringUtils.hasText(requestDto.getName())) this.name = requestDto.getName();
    }
}
